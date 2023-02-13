package ru.ancap.framework.command.api.commands.object.executor;

import ru.ancap.framework.command.api.commands.object.event.CommandDispatch;

/**
 * Next-gen CommandExecutor - больше не является callable, а значит, может быть полностью асинхронным. Зачем это нужно?
 * Изначально, CommandExecutor, представляемый Bukkit'ом, имеет callable метод
 * {@snippet :
 *  List<String> onTabComplete(...);
 * }
 * Не сложно догадаться, что это пример абсолютного говнокода, особенно учитывая что этот метод вызывается в основном потоке
 * сервера - если, например, для получения таб комплитов надо обратиться к удалённой базе данных, до которой пинг 100, весь
 * сервер придётся остановить на 100 мс.
 */
public interface CommandExecutor {

    void on(CommandDispatch dispatch);

}
