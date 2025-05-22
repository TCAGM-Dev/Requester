package io.github.tcagmdev.requester;

public enum StatusCode {
    CONTINUE(100, "Continue", true),
    SWITCHING_PROTOCOLS(101, "Switching Protocols", true),
    PROCESSING(102, "Processing", true),
    EARLY_HINTS(103, "Early Hints", true),

    OK(200, "Ok", true),
    CREATED(201, "Created", true),
    ACCEPTED(202, "Accepted", true),
    NON_AUTHORITATIVE_INFORMATION(203, "Non-Authoritative Information", false),
    NO_CONTENT(204, "No Content", false),
    RESET_CONTENT(205, "Reset Content", false),
    PARTIAL_CONTENT(206, "Partial Content", true),
    MULTI_STATUS(207, "Multi-Status", true),
    ALREADY_REPORTED(208, "Already Reported", false),
    IM_USED(226, "IM Used", false),

    MULTIPLE_CHOICES(300, "Multiple Choices", true),
    MOVED_PERMANENTLY(301, "Moved Permanently", true),
    FOUND(302, "Found", true),
    SEE_OTHER(303, "See Other", true),
    NOT_MODIFIED(304, "Not Modified", false),
    USE_PROXY(305, "Use Proxy", false),
    SWITCH_PROXY(306, "Switch Proxy", false),
    TEMPORARY_REDIRECT(307, "Temporary Redirect", true),
    PERMANENT_REDIRECT(308, "Permanent Redirect", true),

    BAD_REQUEST(400, "Bad Request", false),
    UNAUTHORIZED(401, "Unauthorized", false),
    PAYMENT_REQUIRED(402, "Payment Required", false),
    FORBIDDEN(403, "Forbidden", false),
    NOT_FOUND(404, "Not Found", false),
    METHOD_NOT_ALLOWED(405, "Method Not Allowed", false),
    NOT_ACCEPTABLE(406, "Not Acceptable", false),
    PROXY_AUTHENTICATION_REQUIRED(407, "Proxy Authentication Required", false),
    REQUEST_TIMEOUT(408, "Request Timeout", false),
    CONFLICT(409, "Conflict", false),
    GONE(410, "Gone", false),
    LENGTH_REQUIRED(411, "Length Required", false),
    PRECONDITION_FAILED(412, "Precondition Failed", false),
    PAYLOAD_TOO_LARGE(413, "Payload Too Large", false),
    URI_TOO_LONG(414, "URI Too Long", false),
    UNSUPPORTED_MEDIA_TYPE(415, "Unsupported Media Type", false),
    RANGE_NOT_SATISFIABLE(416, "Range Not Satisfiable", false),
    EXPECTATION_FAILED(417, "Expectation Failed", false),
    IM_A_TEAPOT(418, "I'm a teapot", false),
    MISDIRECTED_REQUEST(421, "Misdirected Request", false),
    UNPROCESSABLE_CONTENT(422, "Unprocessable Content", false),
    LOCKED(423, "Locked", false),
    FAILED_DEPENDENCY(424, "Failed Dependency", false),
    TOO_EARLY(425, "Too Early", false),
    UPGRADE_REQUIRED(426, "Upgrade Required", false),
    PRECONDITION_REQUIRED(428, "Precondition Required", false),
    TOO_MANY_REQUESTS(429, "Too Many Requests", false),
    REQUEST_HEADER_FIELDS_TOO_LARGE(431, "Request Header Fields Too Large", false),
    UNAVAILABLE_FOR_LEGAL_REASONS(451, "Unavailable For Legal Reasons", false),

    INTERNAL_SERVER_ERROR(500, "Internal Server Error", false),
    NOT_IMPLEMENTED(501, "Not Implemented", false),
    BAD_GATEWAY(502, "Bad Gateway", false),
    SERVICE_UNAVAILABLE(503, "Service Unavailable", false),
    GATEWAY_TIMEOUT(504, "Gateway Timeout", false),
    HTTP_VERSION_NOT_SUPPORTED(505, "HTTP Version Not Supported", false),
    VARIANT_ALSO_NEGOTIATES(506, "Variant Also Negotiates", false),
    INSUFFICIENT_STORAGE(507, "Insufficient Storage", false),
    LOOP_DETECTED(508, "Loop Detected", false),
    NOT_EXTENDED(510, "Not Extended", false),
    NETWORK_AUTHENTICATION_REQUIRED(511, "Network Authentication Required", false);

    private final int code;
    private final String readableName;
    private final boolean success;

    StatusCode(int code, String readableName, boolean success) {
       this.code = code;
       this.readableName = readableName;
       this.success = success;
    }

    public int getCode() {
        return this.code;
    }
    public String getReadableName() {
        return this.readableName;
    }
    public boolean isSuccess() {
        return this.success;
    }

    public String toString() {
        return String.format("%d %s", this.code, this.readableName);
    }

    public static StatusCode fromInt(int code) {
        for (StatusCode value : StatusCode.values()) {
            if (value.code == code) return value;
        }
        return null;
    }
}