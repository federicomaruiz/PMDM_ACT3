package com.utad.ideas.ui.activities

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.PackageManagerCompat
import androidx.lifecycle.lifecycleScope
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.utad.ideas.R
import com.utad.ideas.databinding.ActivityCreateBinding
import com.utad.ideas.room.model.Ideas
import com.utad.ideas.ui.application.MyApplication
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CreateActivity : AppCompatActivity() {

    private lateinit var _binding: ActivityCreateBinding
    private val binding: ActivityCreateBinding get() = _binding

    private lateinit var timeValue: String
    private lateinit var priorityValue: String
    private lateinit var photoValue : Bitmap

    private var selectedPhoto: Bitmap? = null

    private  val permissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()){isGranted:Boolean ->
        if(isGranted){
            openGallery()
        }else{
            showPermissionDialog()
        }

    }

    private var settingsLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            //Una vez vuelve el usuario de ajustes, volvemos a comprobar si ha concedido los permisos
            permissonListen()
        }

    private val imageLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            /**Esperamos el resultado de la selección, si ha sido RESULT_OK (lo hace android por debajo)
             * podemos comprobar si trae información consigo
             */
            if (result.resultCode == RESULT_OK) {
                //Recogemos los datos recibidos en el result
                val data: Intent? = result.data
                // Si no son nulos, viene foto
                if (data != null) {
                    //Recojo la ruta de la imagen seleccionada
                    val selectedImage: Uri? = data.data
                    // Llamo a la función de conversión (copiadla y pegadla, no hay que pensar como hacerla)
                    selectedPhoto = convertUriToBitmap(selectedImage)

                    /** Esto es la previsualizaición. Con el BitMap(foto) que hemos obtenido, lo pntamos en la imageView*/
                     binding.ivPhoto.setImageBitmap(selectedPhoto)
                     photoValue = selectedPhoto!!
                } else {
                    Toast.makeText(this, "OYE, NO HAY FOTO", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "OYE, NO HAY FOTO", Toast.LENGTH_SHORT).show()
            }
        }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityCreateBinding.inflate(layoutInflater)
        setContentView(binding.root)
        checkValueTime()
        checkValuePriority()
        addIdeaListen()
        binding.btnGallery.setOnClickListener {
            permissonListen()
        }

    }

    private fun permissonListen() {
        val permission: String = getImagePermission()
        val isPermissionGranted: Boolean =
            ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED
        val shouldRequestRationale: Boolean = ActivityCompat.shouldShowRequestPermissionRationale(this,permission)

        if(isPermissionGranted){
            openGallery()
        }else if (shouldRequestRationale){
            showPermissionDialog()
        }else{
            permissionLauncher.launch(permission)
        }

    }

    private fun showPermissionDialog() {
        MaterialAlertDialogBuilder(this)
            .setTitle(R.string.dialog_denied_permission_title)
            .setMessage(R.string.dialog_denied_permission_message)
            .setPositiveButton(R.string.dialog_denied_permission_button_text) { _, _ ->
                goToSettings()
            }.setNegativeButton(R.string.dialog_denied_permission_button_negative_text) { _, _ ->
                finish()
            }.show()
    }


    private fun goToSettings() {
        /**
         * Esta es una función que no es necesaria que pongáis,
         * os la dejo como extra para el que quiera aprender más.
         */
        val intent = Intent()
        //Indico que quiero abrir los ajustes de android
        intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
        //En la pantalla predefinida
        intent.addCategory(Intent.CATEGORY_DEFAULT)
        //Le indico de qué aplicación (la mia) mediante el -> "package:" + application.packageName
        intent.data = Uri.parse("package:" + application.packageName)
        //Lanzo el intent de abrir los ajustes de la app.
        //Haced control + click para ver que se ahace cuando recibamos el resultado
        settingsLauncher.launch(intent)
    }

    // endregion ----- PERMISOS

    //region ----- GALERÍA
    private fun openGallery() {
        /**
         * Creamos un intent en el que le indicamos a Android que va a ser un intent de "selección", pick.
         * Y le especificamos con el "MediaStore.Images.Media.EXTERNAL_CONTENT_URI" que queremos que
         * nos devuelva un URI. Que es el enlace o ruta interna del archivo que seleecione el usuario.
         * */
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        /** Especificamos que sólo queremos que se puedan seleccionar imágenes */
        intent.type = "image/*"

        /**
         * Con el launcher de la galería, lanzamos el intent de selección para poder leer
         * el resultado de la selección.
         * CTRL/ + CLIC sobre el "imageLauncher" para ver qué se hace en él.
         * */
        imageLauncher.launch(intent)
    }

    private fun convertUriToBitmap(uri: Uri?): Bitmap? {
        /**
         * Esta función no necesitais entenderla.
         * Solo lee el archivo en la ruta que nos llega y la comprime si es muy grande.
         * Devuelve un BitMap para que la podemos guardar en Room y pintar en la vista.
         */
        try {
            //A partir del Uri de la imagen recibida, obtenemos el Bitmap
            val inputStream = contentResolver.openInputStream(uri!!)
            val image = BitmapFactory.decodeStream(inputStream)
            //Si es una imagen pequeña devolveremos el Bitmap
            if (image.byteCount <= 2500000) {
                return image
            } else {
                //En caso de no serlo, comprimiremos la imagen hasta que se pueda guardar en Room
                var compressedImage = image
                do {
                    var scaleWidth = compressedImage.width / 2
                    var scaleHeight = compressedImage.height / 2

                    compressedImage =
                        Bitmap.createScaledBitmap(image, scaleWidth, scaleHeight, true)
                } while (compressedImage.byteCount >= 2500000)
                //Cuando sea lo suficientemente pequeña, devolveremos la imagen comprimida
                return compressedImage
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }

    private fun getImagePermission(): String {
        if (Build.VERSION.SDK_INT >= 33) {
            return Manifest.permission.READ_MEDIA_IMAGES
        } else {
            return Manifest.permission.READ_EXTERNAL_STORAGE
        }
    }

    private fun addIdeaListen() {
        binding.btnSave.setOnClickListener {
            addNewIdea()
        }
    }

    private fun addNewIdea() {
        val name = binding.etIdeaName.text.toString().trim()
        val description = binding.etIdeaDescription.text.toString().trim()
        if (!name.isNullOrEmpty() && !description.isNullOrEmpty()) {
            saveInDataBase(name, description)
        } else {
            Toast.makeText(this, "Completar todos los campos", Toast.LENGTH_SHORT).show()
        }
    }

    private fun saveInDataBase(name: String, description: String) {
        lifecycleScope.launch(Dispatchers.IO) {
            val idea = Ideas(0, name, description, timeValue, priorityValue, photoValue )
            (application as MyApplication).dataBase.ideasDao().saveIdeaList(idea)

            withContext(Dispatchers.Main) {
                finish()
            }
        }
    }

    private fun checkValueTime() {
        binding.cbPendiente.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                binding.cbProgreso.isChecked = false
                binding.cbTerminado.isChecked = false
                timeValue = binding.cbPendiente.text.toString()
                binding.btnSave.isEnabled = true
            } else {
                binding.btnSave.isEnabled = false
            }
        }

        binding.cbProgreso.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                binding.cbPendiente.isChecked = false
                binding.cbTerminado.isChecked = false
                timeValue = binding.cbProgreso.text.toString()
                binding.btnSave.isEnabled = true
            } else {
                binding.btnSave.isEnabled = false
            }
        }

        binding.cbTerminado.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                binding.cbPendiente.isChecked = false
                binding.cbProgreso.isChecked = false
                timeValue = binding.cbTerminado.text.toString()
                binding.btnSave.isEnabled = true
            } else {
                binding.btnSave.isEnabled = false
            }

        }
    }

    private fun checkValuePriority() {
        binding.cbBaja.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                binding.cbMedia.isChecked = false
                binding.cbAlta.isChecked = false
                priorityValue = binding.cbBaja.text.toString()
                binding.btnSave.isEnabled = true
            } else {
                binding.btnSave.isEnabled = false
            }
        }

        binding.cbMedia.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                binding.cbBaja.isChecked = false
                binding.cbAlta.isChecked = false
                priorityValue = binding.cbMedia.text.toString()
                binding.btnSave.isEnabled = true
            } else {
                binding.btnSave.isEnabled = false
            }
        }

        binding.cbAlta.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                binding.cbBaja.isChecked = false
                binding.cbMedia.isChecked = false
                priorityValue = binding.cbAlta.text.toString()
                binding.btnSave.isEnabled = true
            } else {
                binding.btnSave.isEnabled = false
            }

        }
    }

}