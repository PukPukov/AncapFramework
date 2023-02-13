Vi is Anglii? [Check English readme!](https://github.com/ancap-dev/AncapFramework/wiki/English-Readme)

# AncapFramework

![Codacy Badge](https://img.shields.io/codacy/grade/85187fe25a954ba7b9331d1fe51deb25?style=flat-square) ![Sonar Tech Debt](https://img.shields.io/sonar/tech_debt/ancap-dev_AncapFramework?server=https%3A%2F%2Fsonarcloud.io&style=flat-square) ![Lines of code](https://img.shields.io/tokei/lines/github/ancap-dev/AncapFramework?style=flat-square)

Грамотно спроектированный фреймворк для Minecraft-разработки.

# Обзор

Фреймворк решает большинство рутинных задач, возникающих при создании плагина для Bukkit - с особым акцентом на высоконагруженные, многофункциональные, многоязыковые плагины с большим количеством кода и логики.

# Подключение
![Release](https://jitpack.io/v/ancap-dev/AncapFramework.svg?style=flat-square)

По задумке AncapFramework - модульный проект, и для того, чтобы работать с ним, надо каждый модуль подключать отдельно, в большом проекте собирая десяток зависимостей. Но репозиторий, где хостится AncapFramework, имеет другое мнение на этот счёт, и свалил всё в кучу (даже вместе с internal-логикой, ироды!), поэтому всё сразу можно подключить следующим кодом в Maven:

```xml
<repository>
    <id>jitpack.io</id>
    <url>https://jitpack.io</url>
</repository>

<dependency>
    <groupId>com.github.ancap-dev</groupId>
    <artifactId>AncapFramework</artifactId>
    <version>смотри выше</version>
    <scope>provided</scope>
</dependency>
```
(на самом деле джитпак позволяет подключать только некоторые модули, но в этом не особо много смысла, так как версионирование все еще идет по версии релиза, а не модуля)

# Вики

Всю информацию о фреймворке можно найти на [вики](https://github.com/ancap-dev/AncapFramework/wiki).
