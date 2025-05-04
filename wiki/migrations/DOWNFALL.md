Данный мануал описывает миграцию в эпохе "падения" — через поломки, внедрённые с версии 1.7-pre.9 до актуальной версии.

В основном изменения касались модуля команд. В частности:
- Командные ивенты выполнявшие миллион и ни одной задачи одновременно удалены. Частичной заменой для них является новый модуль командных исключений: для ивентов, отображающих ошибку, можно просто выбросить исключение.
  - Пример кода:
      - До:
    ```java
    if (notEnoughPerms) {
        Bukkit.getPluginManager().callEvent(new NotEnoughPermissionsEvent(..., ...));
        return;
    }
    ```
      - После:
    ```java
    if (notEnoughPerms) {
        throw new UnpermittedActionException(..., ...);
    }
    ```
- LeveledCommand полностью переработан. Вместо ручного парсинга аргументов теперь выдан инструмент LCParseState.
  - Пример кода:
    - До:
    ```java
    LeveledCommand parseState = dispatch.command();
    if (parseState.isRaw()) {
        Bukkit.getPluginManager().callEvent(new NotEnoughArgumentsEvent(dispatch.source().sender(), 2));
        return;
    }
    String languageOne = parseState.nextArgument();
    parseState = parseState.withoutArgument();
    if (parseState.isRaw()) {
        Bukkit.getPluginManager().callEvent(new NotEnoughArgumentsEvent(dispatch.source().sender(), 1));
        return;
    }
    String languageTwo = parseState.nextArgument();
    //parseState = parseState.withoutArgument();
    ```
    - После:
    ```java
    LCParseState onLanguageOne = dispatch.command().step(noArgument(() -> new LAPIText(Artifex.class, "arguments.compared-language")));
    LCParseState onLanguageTwo = onLanguageOne.command().step(noArgument(() -> new LAPIText(Artifex.class, "arguments.compared-language")));
    ```
- Слово Message в семействе CallableMessage заменено на Text. 
  - Примеры: CallableMessage.java -> CallableText.java, LAPIMessage.java -> LAPIText.java
- Слово Registry в контексте модуля sql баз данных заменено на Repository.
  - Примеры: Registry.java -> Repository.java, RegistryInitialization.java -> RepositoryInitialization.java
- Возможно, список не исчерпывающий. Как всегда при миграциях, лучший помощник это исходники автора поломок, т.к. автор первый кто с ними столкнулся и уже их исправил раз версия опубликована