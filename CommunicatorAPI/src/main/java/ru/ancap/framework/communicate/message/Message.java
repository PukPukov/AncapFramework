package ru.ancap.framework.communicate.message;

import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import ru.ancap.framework.communicate.MiniMessageMapper;
import ru.ancap.framework.communicate.modifier.Modifier;

@ToString(callSuper = true) @EqualsAndHashCode
@RequiredArgsConstructor
public class Message implements CallableMessage {

    public static final CallableMessage EMPTY = new Message("");

    private final String base;
    private final Modifier[] modifiers;

    public Message(Object base, Modifier... modifiers) {
        this(String.valueOf(base), modifiers);
    }

    @Override
    public String call(String receiverId) {
        String operated = base;

        // Base modifiers—legacy color codes (with "&" symbol) and \n support
        {
            operated = MiniMessageMapper.mapLegacy(operated);
            operated = operated.replace("\n", "<newline>");
            operated = operated.replaceAll("<newline>", "<newline><reset>");
        }

        // PlaceholderAPI modifiers. Currently not supported because I haven't created the architecture yet
        {

        }

        // custom modifiers
        for (Modifier modifier : modifiers) operated = modifier.apply(operated, receiverId);
        return operated;
    }

}
