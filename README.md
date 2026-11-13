# Wikipedia · Mobile автотесты

Дипломный проект по мобильному тестированию: onboarding, поиск и открытие статьи
в Wikipedia для Android. Дополнительный iOS smoke-тест проверяет приложение UI Elements
в BrowserStack — это отдельное приложение, не Wikipedia.

![Java](https://img.shields.io/badge/Java-17-ED8B00?logo=openjdk&logoColor=white) ![JUnit](https://img.shields.io/badge/JUnit-5-25A162?logo=junit5&logoColor=white) ![Gradle](https://img.shields.io/badge/Gradle-02303A?logo=gradle&logoColor=white) ![Allure](https://img.shields.io/badge/Allure-Report-FF6B35) ![Appium](https://img.shields.io/badge/Appium-4724-662D91?logo=appium&logoColor=white)

[Репозиторий](https://github.com/RenataFatykhov/mobile_tests_local_and_remote) ·
[Ручные тест-кейсы](docs/manual-test-cases.md)

## Архитектура

- Screen Objects отделяют сценарии от локаторов: onboarding, поиск, статья, UI Elements.
- Appium-драйвер выбирается через `deviceHost`, настройки читаются библиотекой OWNER.
- JUnit-теги разделяют Android, iOS и инфраструктурные проверки.
- Allure-шаги, скриншоты, XML страницы; для BrowserStack также ссылка на видео.
- Загрузчик APK проверяет тип файла и не сохраняет неудачную загрузку как рабочий APK.

| Стенд | Android Wikipedia | iOS UI Elements |
| --- | --- | --- |
| Эмулятор (`emulation`) | Да | Нет |
| Реальный телефон (`real`) | Да | Нет |
| BrowserStack (`browserstack`) | Да | Да |

Стек: Java 17, Gradle, JUnit 5, Selenide, Appium, OWNER, Allure.

## Тесты

- `AndroidOnboardingTests`: четыре экрана onboarding с проверкой заголовка на каждом,
  завершение onboarding и проверка главной страницы.
- `AndroidSearchTests`: поиск Appium и открытие статьи из предыдущего задания.
- `IosTests`: проверка UI Elements из предыдущего задания (BrowserStack).

`deviceHost` выбирает отдельный драйвер и OWNER-конфиг:
`emulation` (по умолчанию), `real`, `browserstack`.
`platform` выбирает JUnit-тег: `android` (по умолчанию) или `ios`.
Локальные драйверы поддерживают только Android.

## Эмулятор

Запустить Appium: `appium server --base-path /wd/hub`.
В `src/test/resources/config/emulation.properties` указан существующий AVD
`New_Device_API_34`. Appium запустит его, если он ещё выключен.

Локально в `apps/wikipedia.apk` подготовлена Wikipedia 2.7.50464 (четыре экрана
onboarding на английском языке). APK не включается в Git. Для повторной загрузки:

```bash
mkdir -p apps
curl -fL https://releases.wikimedia.org/mobile/android/wikipedia/stable/wikipedia-2.7.50464-r-2023-12-11.apk -o apps/wikipedia.apk
```

```bash
./gradlew test -DdeviceHost=emulation --tests tests.AndroidOnboardingTests
./gradlew test -DdeviceHost=emulation -Dplatform=android
```

## Реальный Android

Включить USB debugging, разрешить подключение и узнать serial командой `adb devices`.
Закрыть сессию Appium Inspector перед запуском тестов на том же телефоне.
Использовать тестовое приложение: Appium сбрасывает его данные перед тестом,
чтобы onboarding всегда начинался с первого экрана. Для уже установленной более
новой Wikipedia понадобится совместимый APK; старый APK поверх неё не установится.

```bash
./gradlew test -DdeviceHost=real -Ddevice.udid=YOUR_SERIAL --tests tests.AndroidOnboardingTests
```

Настройки переопределяются через `-D` либо через игнорируемые Git файлы
`emulation.local.properties`, `real.local.properties` в корне проекта, например:

```properties
device.udid=YOUR_SERIAL
android.app=/absolute/path/to/wikipedia.apk
android.app.package=org.wikipedia
android.app.activity=org.wikipedia.main.MainActivity
```

## BrowserStack

В `src/test/resources/config/browserstack.properties` перенесены настройки приложений
из `mobile_tests`. Ключи хранятся отдельно в игнорируемом `browserstack.local.properties`.
Можно также использовать переменные окружения `BROWSERSTACK_USER`, `BROWSERSTACK_KEY`.
Приоритет: параметры `-D`, окружение, локальный файл, файл из resources.
Если загруженное приложение истекло, загрузить его снова и передать новый `android.app`
или `ios.app` (`bs://...`). Для onboarding нужна сборка Wikipedia с четырьмя экранами
и включённым onboarding. Локальные локаторы поддерживают пакеты org.wikipedia и
org.wikipedia.alpha.

```bash
./gradlew test -DdeviceHost=browserstack -Dplatform=android
./gradlew test -DdeviceHost=browserstack -Dplatform=ios
```

## Проверка и отчёты

```bash
./gradlew testClasses
./gradlew unitTest   # 4 проверки загрузчика APK без Appium
./gradlew allureReport
```

HTML-отчёт JUnit: `build/reports/tests/test/index.html`.
Allure-результаты: `build/allure-results`. Скриншот и XML прикладываются на всех
стендах, видео запрашивается только для BrowserStack после закрытия сессии.
Повторный запуск без изменений можно выполнить с `--rerun-tasks`.

## Предыдущие результаты локальной проверки

25.10.2026, AVD `New_Device_API_34`, Android 14, Appium 2.4.1:

- `./gradlew testClasses` — успешно.
- `AndroidOnboardingTests` — 1 тест прошёл, проверены четыре экрана и выход на главную.
- `AndroidSearchTests` — 2 теста прошли (отдельный запуск).
- Запуск iOS с `deviceHost=real` отклоняется понятной ошибкой конфигурации.

### Реальный телефон

25.10.2026, 22:09 (МСК), REALME C71 (`RMX5303`), Android 15, Appium 2.4.1.

Команда запуска:

```bash
./gradlew test -DdeviceHost=real -Ddevice.udid=YOUR_SERIAL --tests tests.AndroidOnboardingTests
```

Результат: **1 тест прошёл, 0 ошибок, 0 падений, 0 пропусков**.
`AndroidOnboardingTests` проверил заголовки всех четырёх экранов onboarding,
завершение знакомства и открытие главной страницы Wikipedia.
Время теста по JUnit-отчёту — 23,746 с.

Результат подтверждён `build/test-results/test/TEST-tests.AndroidOnboardingTests.xml`
и статусом `passed` в Allure. Перед успешным прогоном был запущен Appium
с параметром `--base-path /wd/hub`.

## Ручное тестирование

[Тест-кейсы Wikipedia](docs/manual-test-cases.md): onboarding, поиск, открытие статьи,
отмена поиска и поведение без сети. Статус ручных кейсов — «Не выполнен» до отдельного прогона.

## Автор

Рената Фатыхова · [Telegram @renata_fatykhov](https://t.me/renata_fatykhov)
