> [Read this page on English on Wiki](https://github.com/ancap-dev/AncapFramework/wiki)

# AncapFramework

![Codacy Badge](https://img.shields.io/codacy/grade/85187fe25a954ba7b9331d1fe51deb25?style=flat-square) ![Code Climate maintainability](https://img.shields.io/codeclimate/maintainability-percentage/PukPukov/AncapFramework-1?style=flat-square) ![Sonar Tech Debt](https://img.shields.io/sonar/tech_debt/ancap-dev_AncapFramework?server=https%3A%2F%2Fsonarcloud.io&style=flat-square) ![Lines of code](https://img.shields.io/tokei/lines/github/PukPukov/AncapFramework-1?style=flat-square)

Фреймворк для Minecraft-разработки на Bukkit с восхитительно чистым ООП-кодом.


# Обзор

Фреймворк решает большинство рутинных задач, возникающих при создании плагина для Bukkit - с особым акцентом на высоконагруженные, многофункциональные, многоязыковые плагины с большим количеством кода и логики, а также заставляет разработчика писать код чисто и не пользоваться плохими практиками.

# Подключение
## Maven
### Repository

     <repository>  
	     <id>AncapFramework-mvn-repo</id>  
	     <url>https://raw.github.com/ancap-dev/AncapFramework/mvn-repo/</url>  
	     <snapshots> 
		     <enabled>true</enabled>  
		     <updatePolicy>always</updatePolicy>  
	     </snapshots>
	 </repository>

### Dependency

Каждый модуль имеет свою зависимость. Подключайте только то, что вам необходимо. 

     <dependency>  
	     <groupId>ru.ancap</groupId>  
	     <artifactId>EventAPI</artifactId>  
	     <version>1.4.0</version>  
	     <scope>provided</scope>  
     </dependency>

# Вики

Другие языки, а также гайд для самых маленьких по использованию фреймворка можно найти на [вики](https://github.com/ancap-dev/AncapFramework/wiki).
