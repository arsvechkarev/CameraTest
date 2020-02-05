package com.arsvechkarev.letta

import android.Manifest.permission.CAMERA
import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.arsvechkarev.letta.media.ImagesListFragment
import timber.log.Timber

class MainActivity : AppCompatActivity(R.layout.activity_main) {
  
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    Timber.plant(Timber.DebugTree())
    ActivityCompat.requestPermissions(
      this,
      arrayOf(CAMERA, READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE),
      1
    )
  }
  
  override fun onRequestPermissionsResult(
    requestCode: Int,
    permissions: Array<out String>,
    grantResults: IntArray
  ) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    supportFragmentManager.beginTransaction()
        .replace(R.id.fragmentContainer, ImagesListFragment())
        .commit()
  }
}