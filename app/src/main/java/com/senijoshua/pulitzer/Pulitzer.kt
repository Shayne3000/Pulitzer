package com.senijoshua.pulitzer

import android.app.Application
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.disk.DiskCache
import coil.memory.MemoryCache
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class Pulitzer : Application(), ImageLoaderFactory {

    /**
     * Override the image loader used globally in the app to disable respecting the
     * directive sent from remote image servers in the cache headers, and setup disk and memory
     * caching for this custom Image Loader.
     * This is so that regardless of the directive sent, we would serve
     * images from the disk cache. Nominally, the directive can be to not store/serve images
     * in/from a local disk cache.
     */
    override fun newImageLoader(): ImageLoader {
        // See https://github.com/coil-kt/coil/issues/1857 and
        // https://stackoverflow.com/questions/72981927/coil-image-caching-not-working-with-jetpack-compose
        // for more context.
        return ImageLoader.Builder(this)
            .memoryCache {
                MemoryCache.Builder(this)
                    .maxSizePercent(0.25)
                    .build()
            }
            .diskCache {
                DiskCache.Builder()
                    .directory(this.cacheDir.resolve("image_cache"))
                    .maxSizePercent(0.02)
                    .build()
            }
            .respectCacheHeaders(false)
            .build()
    }
}
