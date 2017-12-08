package com.wonderkiln.camerakit;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static com.wonderkiln.camerakit.CameraKit.Constants.SURFACE_SURFACE;
import static com.wonderkiln.camerakit.CameraKit.Constants.SURFACE_TEXTURE;

/**
 * Created by Matt on 08/12/2017.
 */
@IntDef({SURFACE_TEXTURE, SURFACE_SURFACE})
@Retention(RetentionPolicy.SOURCE)
public @interface SurfaceType {
}
