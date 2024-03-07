package com.restaurant.menuservice.constants;

import java.util.List;

public class ErrorConstants {
    public static final List<String> ERROR_CAUSES_CONFLICT = List.of("The request could not be completed due to a conflict with the current state of the target resource.");
    public static final List<String> ERROR_CAUSES_INTERNAL_SERVER_ERROR = List.of("Internal server error due to misconfiguration.", "The server might be experiencing technical difficulties.", "Database connection issues could be causing this error.");
    public static final List<String> ERROR_CAUSES_FORBIDDEN = List.of("Access denied. Client credentials or permissions are insufficient to access the resource.", "Lack of proper authorization to access the requested resource.", "IP address might be blocked, preventing access.");
    public static final List<String> ERROR_CAUSES_UNAUTHORIZED = List.of("Authentication failure. Provided credentials are invalid or missing.", "User needs to provide valid credentials to access the resource.", "Token expiration might be causing the authentication issue.");
    public static final List<String> ERROR_CAUSES_BAD_REQUEST = List.of("Invalid input. Server couldn't understand the request due to malformed syntax or invalid parameters.", "Client should review and correct the request format.", "Missing required parameters in the request.");
    public static final List<String> ERROR_CAUSES_METHOD_NOT_ALLOWED = List.of("Unsupported method. Request method used is not supported for the given resource.", "Client should use a different HTTP method that the server supports.", "Cross-origin restrictions might be causing this error.");
    public static final List<String> ERROR_CAUSES_NOT_ACCEPTABLE = List.of("Unacceptable content. Server cannot produce a response matching the list of acceptable values defined in the request's headers.", "Client may need to adjust the request headers to accept different content types.", "Content negotiation might be failing due to conflicting preferences.");
    public static final List<String> TROUBLESHOOTING_STEPS_NOT_FOUND = List.of("Double-check the URL and make sure it's accurate.", "If the resource was recently moved, update your request accordingly.", "If you're trying to access a specific version, ensure the version is correct.", "If you believe this is a mistake, please contact our support team at support@example.com.");
    public static final List<String> TROUBLESHOOTING_STEPS_CONFLICT = List.of("Review the data being submitted for conflicts with the current state of the resource.", "Check if there are any ongoing processes affecting the resource.", "Consider communicating with relevant stakeholders to resolve the conflict.");
    public static final List<String> TROUBLESHOOTING_STEPS_INTERNAL_SERVER_ERROR = List.of("Check server logs for any error messages or exceptions.", "Review recent changes to server configuration or code.", "Ensure that all required services and databases are up and running.");
    public static final List<String> TROUBLESHOOTING_STEPS_FORBIDDEN = List.of("Check if the user has the necessary permissions to access the resource.", "Review the authorization logic and policies in place.", "Check if IP addresses are properly whitelisted.");
    public static final List<String> TROUBLESHOOTING_STEPS_UNAUTHORIZED = List.of("Verify the correctness of the provided credentials.", "Check the expiration date of any tokens used for authentication.", "Ensure that the authentication process is functioning correctly.");
    public static final List<String> TROUBLESHOOTING_STEPS_BAD_REQUEST = List.of("Check the request payload for correct syntax and valid parameters.", "Review the API documentation to ensure the correct request format.", "Ensure that required fields are provided and properly formatted.");
    public static final List<String> TROUBLESHOOTING_STEPS_METHOD_NOT_ALLOWED = List.of("Verify that you are using the correct HTTP method for the resource.", "Check if the server supports the HTTP method you are using.", "Ensure that there are no network or proxy issues causing this error.");
    public static final List<String> TROUBLESHOOTING_STEPS_NOT_ACCEPTABLE = List.of("Check the 'Accept' headers in your request for the expected content types.", "Verify that the server is capable of producing the desired content type.", "Consider adjusting your content negotiation preferences in the request.");
    public static final List<String> NOT_FOUND_CAUSES = List.of("The provided URL is maybe incorrect.", "The resource may has been deleted or moved.", "Check the spelling and case of the resource name.");

    private ErrorConstants() {

    }
}
