package com.example.focar

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.browser.trusted.TrustedWebActivityDisplayMode
import androidx.browser.trusted.TrustedWebActivityIntentBuilder
import com.google.androidbrowserhelper.trusted.TwaLauncher

class MainActivity : AppCompatActivity() {

    private lateinit var twaLauncher: TwaLauncher
    private var isLaunched = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        twaLauncher = TwaLauncher(this)
        checkPermissionsAndLaunch()
    }

    private fun checkPermissionsAndLaunch() {
        val permissionsNeeded = mutableListOf<String>()
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permissionsNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val btPermissions = arrayOf(
                Manifest.permission.BLUETOOTH_CONNECT,
                Manifest.permission.BLUETOOTH_SCAN,
                Manifest.permission.BLUETOOTH_ADVERTISE
            )
            for (p in btPermissions) {
                if (ContextCompat.checkSelfPermission(this, p) != PackageManager.PERMISSION_GRANTED) {
                    permissionsNeeded.add(p)
                }
            }
        }

        if (permissionsNeeded.isNotEmpty()) {
            ActivityCompat.requestPermissions(this, permissionsNeeded.toTypedArray(), PERMISSION_REQUEST_CODE)
        } else {
            launchTwa()
        }
    }

    private fun launchTwa() {
        if (!isLaunched) {
            isLaunched = true

            // CONFIGURAÇÃO PARA ESCONDER A BARRA:
            // 1. Criamos um Builder para a Intent
            val builder = TrustedWebActivityIntentBuilder(Uri.parse(TARGET_URL))
                .setToolbarColor(Color.parseColor("#00D9FF")) // Cor do seu tema Nuxt
                // 2. Forçamos o modo Imersivo (esconde até a barra de status se desejar)
                // O parâmetro 'false' no ImmersiveMode indica se a barra deve ser "sticky"
                .setDisplayMode(TrustedWebActivityDisplayMode.ImmersiveMode(false, 0))

            // 3. Lançamos usando o builder customizado
            twaLauncher.launch(builder, null, null, null)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_CODE) {
            launchTwa()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (::twaLauncher.isInitialized) {
            twaLauncher.destroy()
        }
    }

    companion object {
        private const val PERMISSION_REQUEST_CODE = 1001
        // ATUALIZADO: Apontando para o domínio que você validou no Nginx
        private const val TARGET_URL = "https://test-focar-remake.biohealth.com.br"
    }
}
