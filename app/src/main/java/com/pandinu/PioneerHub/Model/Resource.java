package com.pandinu.PioneerHub.Model;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;

@ParseClassName("Resource")
public class Resource extends ParseObject {

    // Must match names in back4app dashboard
    public static final String KEY_NAME = "name";
    public static final String KEY_THUMBNAIL = "thumbnail";
    public static final String KEY_WEBSITE_URL = "websiteUrl";
    public static final String KEY_DESCRIPTION = "description";

    // Name
    public String getName() {
        return getString(KEY_NAME);
    }
    public void setName(String name) {
        put(KEY_NAME, name);
    }

    // Thumbnail
    public ParseFile getThumbnail() {
        return getParseFile(KEY_THUMBNAIL);
    }
    public void setThumbnail(ParseFile parseFile) {
        put(KEY_THUMBNAIL, parseFile);
    }

    // Website URL
    public String getWebsiteUrl() {
        return getString(KEY_WEBSITE_URL);
    }
    public void setWebsiteUrl(String websiteUrl) {
        put(KEY_WEBSITE_URL, websiteUrl);
    }

    // Description
    public String getDescription() {
        return getString(KEY_DESCRIPTION);
    }
    public void setDescription(String description) {
        put(KEY_DESCRIPTION, description);
    }

}
