package common.connection;

/**
 * Message witch server send to client
 */
public class AnswerMsg implements Response {
    private static final long serialVersionUID = 666;
    private String msg;
    private Status status;

    public AnswerMsg() {
        msg = "";
        status = Status.FINE;
    }

    /**
     * Clear message
     */
    public AnswerMsg clear() {
        msg = "";
        return this;
    }

    /**
     * set answer that you want to user see
     *
     * @param str String that will bw added to answer
     */
    public AnswerMsg info(Object str) {
        msg = str.toString();// + "\n";
        return this;
    }

    /**
     * set error message
     *
     * @param str Message what happened
     */
    public AnswerMsg error(Object str) {
        msg = /*"Error: " + */str.toString();// + "\n";
        setStatus(Status.ERROR);
        return this;
    }

    /**
     * Add status of message
     *
     * @param st Status of message
     */
    public AnswerMsg setStatus(Status st) {
        status = st;
        return this;
    }

    /**
     * Get message
     *
     * @return Message
     */
    public String getMessage() {
        return msg;
    }

    /**
     * Get status of message
     *
     * @return Status
     */
    public Status getStatus() {
        return status;
    }


    @Override
    public String toString() {
        if (getStatus() == Status.ERROR) {
            return "Err: " + getMessage();
        }
        return getMessage();
    }
}