package sleepy.mollu.server.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.springframework.http.MediaType;

public class SimpleRestAssured {

    public static ExtractableResponse<Response> get(String path) {
        return get(path, null);
    }

    public static ExtractableResponse<Response> get(String path, String token) {
        return thenExtract(givenWithHeaders(token).when().get(path));
    }

    public static ExtractableResponse<Response> post(String path, Object request) {
        return post(path, null, request);
    }

    public static ExtractableResponse<Response> post(String path, String token) {
        return post(path, token, null);
    }

    public static ExtractableResponse<Response> post(String path, String token, Object request) {
        final RequestSpecification given = givenWithHeaders(token);
        if (request != null) {
            given.body(request);
        }

        return thenExtract(given.contentType(MediaType.APPLICATION_JSON_VALUE).when().post(path));
    }

    public static ExtractableResponse<Response> put(String path, String token, Object request) {
        final RequestSpecification given = givenWithHeaders(token);
        if (request != null) {
            given.body(request);
        }

        return thenExtract(given.contentType(MediaType.APPLICATION_JSON_VALUE).when().put(path));
    }

    public static ExtractableResponse<Response> patch(String path, String token, Object request) {
        final RequestSpecification given = givenWithHeaders(token);
        if (request != null) {
            given.body(request);
        }

        return thenExtract(given.contentType(MediaType.APPLICATION_JSON_VALUE).when().patch(path));
    }

    public static ExtractableResponse<Response> delete(String path, String token) {
        final RequestSpecification given = givenWithHeaders(token);

        return thenExtract(given.contentType(MediaType.APPLICATION_JSON_VALUE).when().delete(path));
    }

    private static RequestSpecification givenWithHeaders(String token) {
        final RequestSpecification given = given();
        if (token != null) {
            given.header("Authorization", "Bearer " + token);
        }
        return given;
    }

    public static RequestSpecification given() {
        return RestAssured.given().log().all();
    }

    public static ExtractableResponse<Response> thenExtract(Response response) {
        return response.then().log().all().extract();
    }

    public static <T> T toObject(ExtractableResponse<Response> response, Class<T> clazz) {
        return response.as(clazz);
    }
}
