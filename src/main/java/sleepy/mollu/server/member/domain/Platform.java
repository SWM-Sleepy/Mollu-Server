package sleepy.mollu.server.member.domain;

public enum Platform {
    ANDROID, IOS;

    public static Platform from(String platform) {
        try {
            return Platform.valueOf(platform.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("[" + platform + "]은 올바르지 않은 플랫폼입니다.");
        } catch (NullPointerException e) {
            return null;
        }
    }
}
