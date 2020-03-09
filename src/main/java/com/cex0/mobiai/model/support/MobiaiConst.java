package com.cex0.mobiai.model.support;


import org.springframework.http.HttpHeaders;

import java.io.File;

/**
 * 公共常量
 *
 * @author Cex0
 */
public class MobiaiConst {

    /**
     * 用户的主目录。
     */
    public final static String USER_HOME = System.getProperties().getProperty("user.home");

    /**
     * Temporary directory.
     */
    public final static String TEMP_DIR = System.getProperties().getProperty("java.io.tmpdir");

    /**
     * MOBIAI backup prefix.
     */
    public final static String MOBIAI_BACKUP_PREFIX = "halo-backup-";

    /**
     * Static pages pack prefix.
     */
    public final static String STATIC_PAGE_PACK_PREFIX = "static-pages-";

    /**
     * 默认的主题的名称。
     */
    public final static String DEFAULT_THEME_ID = "caicai_anatole";

    /**
     * 版本常量。(可在生产环境中使用)
     */
    public static final String MOBIAI_VERSION;

    /**
     * 路径分隔符。
     */
    public static final String FILE_SEPARATOR = File.separator;

    /**
     * freemarker模板文件的后缀
     */
    public static final String SUFFIX_FTL = ".ftl";

    /**
     * 自定义freemarker标记方法键。
     */
    public static final String METHOD_KEY = "method";
    /**
     * 网易云音乐短代码前缀
     */
    public static final String NETEASE_MUSIC_PREFIX = "[music:";
    /**
     * 网易云音乐 iframe 代码
     */
    public static final String NETEASE_MUSIC_IFRAME = "<iframe frameborder=\"no\" border=\"0\" marginwidth=\"0\" marginheight=\"0\" width=330 height=86 src=\"//music.163.com/outchain/player?type=2&id=$1&auto=1&height=66\"></iframe>";
    /**
     * 网易云音乐短代码正则表达式
     */
    public static final String NETEASE_MUSIC_REG_PATTERN = "\\[music:(\\d+)\\]";
    /**
     * 哔哩哔哩视频短代码前缀
     */
    public static final String BILIBILI_VIDEO_PREFIX = "[bilibili:";
    /**
     * 哔哩哔哩视频 iframe 代码
     */
    public static final String BILIBILI_VIDEO_IFRAME = "<iframe height=$3 width=$2 src=\"//player.bilibili.com/player.html?aid=$1\" scrolling=\"no\" border=\"0\" frameborder=\"no\" framespacing=\"0\" allowfullscreen=\"true\"> </iframe>";
    /**
     * 哔哩哔哩视频正则表达式
     */
    public static final String BILIBILI_VIDEO_REG_PATTERN = "\\[bilibili:(\\d+)\\,(\\d+)\\,(\\d+)\\]";
    /**
     * YouTube 视频短代码前缀
     */
    public static final String YOUTUBE_VIDEO_PREFIX = "[youtube:";
    /**
     * YouTube 视频 iframe 代码
     */
    public static final String YOUTUBE_VIDEO_IFRAME = "<iframe width=$2 height=$3 src=\"https://www.youtube.com/embed/$1\" frameborder=\"0\" allow=\"accelerometer; autoplay; encrypted-media; gyroscope; picture-in-picture\" allowfullscreen></iframe>";
    /**
     * YouTube 视频正则表达式
     */
    public static final String YOUTUBE_VIDEO_REG_PATTERN = "\\[youtube:(\\w+)\\,(\\d+)\\,(\\d+)\\]";
    /**
     * Github Api url release.
     */
    public final static String HALO_ADMIN_RELEASES_LATEST = "/releases/latest";

    /**
     * user_session
     */
    public static String USER_SESSION_KEY = "user_session";

    /**
     * Github Api url for mobiai-admin release.
     */
    public final static String MOBIAI_ADMIN_RELEASES_LATEST = "https://api.github.com/repos/mobiai-dev/mobiai-admin/releases/latest";

    /**
     * Mobiai admin version regex.
     */
    public final static String MOBIAI_ADMIN_VERSION_REGEX = "mobiai-admin-\\d+\\.\\d+(\\.\\d+)?(-\\S*)?\\.zip";

    public final static String MOBIAI_ADMIN_RELATIVE_PATH = "templates/admin/";

    public final static String MOBIAI_ADMIN_RELATIVE_BACKUP_PATH = "templates/admin-backup/";

    /**
     * Content token header name.
     */
    public final static String API_ACCESS_KEY_HEADER_NAME = "API-" + HttpHeaders.AUTHORIZATION;
    /**
     * Admin token header name.
     */
    public final static String ADMIN_TOKEN_HEADER_NAME = "ADMIN-" + HttpHeaders.AUTHORIZATION;
    /**
     * Admin token param name.
     */
    public final static String ADMIN_TOKEN_QUERY_NAME = "admin_token";
    /**
     * Temporary token.
     */
    public final static String TEMP_TOKEN = "temp_token";
    /**
     * Content api token param name
     */
    public final static String API_ACCESS_KEY_QUERY_NAME = "api_access_key";

    public final static String ONE_TIME_TOKEN_QUERY_NAME = "ott";

    public final static String ONE_TIME_TOKEN_HEADER_NAME = "ott";


    static {
        // Set version
        MOBIAI_VERSION = MobiaiConst.class.getPackage().getImplementationVersion();
    }
}
