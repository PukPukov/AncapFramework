package ru.ancap.framework.communicate.message;

public class ErrorTraceMessage extends WrapperMessage {

    // TODO

    public ErrorTraceMessage(CallableMessage delegate) {
        super(delegate);
    }

    //public ErrorTraceMessage(Throwable throwable) {
    //    super(new MultilineMessage()
    //        CommonMessageDomains.Debug.errorOutputForm,
    //        new Placeholder("exception", throwable.getClass().getName()),
    //        new Placeholder("message", throwable.getMessage()),
    //        throwable.getCause() != null ? new Placeholder("cause")
    //        new Placeholder("cause", new ArgumentPlaceholder(
    //            "cause",
    //            format -> new DeepRepresentation<>(
    //                "inner cause",
    //                format,
    //                CommonMessageDomains.mergeTemplate,
    //                throwable,
    //                throwable1 -> {
    //                    Throwable cause = throwable1.getCause();
    //                    if (cause == null) return new DeepRepresentation.StopRecursion<>();
    //                    else return new DeepRepresentation.ContinueRecursion<>(cause);
    //                }
    //            )
    //        )),
    //        new ArgumentPlaceholder("stack trace", prefix -> new ChatBook<>(
    //            Arrays.asList(throwable.getStackTrace()),
    //            element -> new Message(prefix + element.toString())
    //        ))
    //    ));
    //}
    
}
