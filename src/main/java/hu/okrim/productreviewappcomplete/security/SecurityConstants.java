package hu.okrim.productreviewappcomplete.security;

public class SecurityConstants {
    //Secret key should always be strong (uppercase, lowercase, numbers, symbols) so that nobody can potentially decode the signature.
    public static final String SECRET_KEY = System.getenv("JWT_SECRET_KEY");
    public static final int TOKEN_EXPIRATION = 1800000; // 1800000 milliseconds = 30 minutes.
    public static final String BEARER = "Bearer "; // Authorization : "Bearer " + Token
    public static final String AUTHORIZATION = "Authorization"; // "Authorization" : Bearer Token
    public static final String REGISTER_PATH = "/user/register"; // Public path that users can use to register.
}
