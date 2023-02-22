Vi is Anglii? [Check English readme!](https://github.com/ancap-dev/AncapFramework/wiki/English-Readme)

# AncapFramework

![Codacy Badge](https://img.shields.io/codacy/grade/85187fe25a954ba7b9331d1fe51deb25?style=flat-square) ![Sonar Tech Debt](https://img.shields.io/sonar/tech_debt/ancap-dev_AncapFramework?server=https%3A%2F%2Fsonarcloud.io&style=flat-square) ![Lines of code](https://img.shields.io/tokei/lines/github/ancap-dev/AncapFramework?style=flat-square)

Грамотно спроектированный фреймворк для Minecraft-разработки.

# Обзор

Фреймворк решает большинство рутинных задач, возникающих при создании плагина для Bukkit - с особым акцентом на высоконагруженные, многофункциональные, многоязыковые плагины с большим количеством кода и логики.

# Подключение
![Release](https://jitpack.io/v/ancap-dev/AncapFramework.svg?style=flat-square)

Подключить всё сразу:

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

Подключать по модулям:

```xml
<repository>
    <id>jitpack.io</id>
    <url>https://jitpack.io</url>
</repository>

<dependency>
    <groupId>com.github.ancap-dev.AncapFramework</groupId>
    <artifactId>AncapPluginAPI</artifactId>
    <version>смотри выше</version>
    <scope>provided</scope>
</dependency>
```
Рекомендуется подключать по модулям, по задумке AncapFramework - модульный проект.

# Вики

Всю информацию о фреймворке можно найти на [вики](https://github.com/ancap-dev/AncapFramework/wiki).
