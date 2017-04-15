package com.sagarnileshshah.carouselmvp.util;


import static com.sagarnileshshah.carouselmvp.util.Properties.PHOTO_URL;

import com.sagarnileshshah.carouselmvp.data.models.photo.Photo;

public class MiscHelper {

    public String getPhotoUrl(Photo photo) {
        return String.format(PHOTO_URL, photo.getFarm(), photo.getServer(),
                photo.getId(), photo.getSecret());
    }
}
