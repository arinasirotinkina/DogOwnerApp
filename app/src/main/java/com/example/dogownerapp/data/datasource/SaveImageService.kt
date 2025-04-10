import android.content.Context
import android.net.Uri
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.apache.commons.net.ftp.FTP
import org.apache.commons.net.ftp.FTPClient
import java.io.IOException
import java.io.InputStream

class SaveImageService {
    suspend fun uploadFileToFTP(uri: Uri, context: Context, collection: String, id: String) = withContext(Dispatchers.IO) {
        val ftpClient = FTPClient()

        try {
            ftpClient.connect("arinas8t.beget.tech", 21)
            ftpClient.setConnectTimeout(5000)
            ftpClient.setDataTimeout(5000)
            ftpClient.enterLocalPassiveMode()
            val loginSuccess = ftpClient.login("arinas8t_h", "H123h456!")
            if (!loginSuccess) {
                Log.e("FTP", "Ошибка аутентификации")
                return@withContext
            }
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE)
            val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
            if (inputStream == null) {
                Log.e("FTP", "Не удалось открыть поток для $uri")
                return@withContext
            }
            val remoteFileName = "arinas8t.beget.tech/public_html/photo/$collection/$id"
            val uploadSuccess = ftpClient.storeFile(remoteFileName, inputStream)
            if (uploadSuccess) {
                Log.d("FTP", "Файл успешно загружен: $remoteFileName")
            } else {
                Log.e("FTP", "Ошибка при загрузке файла.")
            }
            inputStream.close()
            ftpClient.logout()
            ftpClient.disconnect()
        } catch (e: IOException) {
            e.printStackTrace()
            Log.e("FTP", "Ошибка: ${e.message}")
        }
    }
    suspend fun renameFileOnFTP(
        collection: String,
        oldFileName: String,
        newFileName: String) = withContext(Dispatchers.IO) {
        val ftpClient = FTPClient()
         try {
            ftpClient.connect("arinas8t.beget.tech", 21)
             ftpClient.connectTimeout = 2000
            ftpClient.setDataTimeout(2000)

            Log.d("FTP", "Подключение успешно!")

            ftpClient.login("arinas8t_h", "H123h456!")

            ftpClient.enterLocalPassiveMode()
             val changeDirSuccess = ftpClient.changeWorkingDirectory("arinas8t.beget.tech/public_html/photo/$collection")
             if (changeDirSuccess) {
                 Log.d("FTP", "Директория изменена на: $collection")
             } else {
                 Log.d("FTP", "Не удалось сменить директорию.")
                 return@withContext false
             }

             val files = ftpClient.listFiles()
             Log.d("FTP", "Список файлов в директории:")
             files.forEach {
                 Log.d("FTP", "Файл: ${it.name}")
             }
             val success = ftpClient.rename(oldFileName, newFileName)

             if (success) {
                 Log.d("FTP", "Файл успешно переименован.")
             } else {
                 Log.d("FTP", "Не удалось переименовать файл.")
             }
            ftpClient.logout()
            success
        } catch (e: IOException) {
            e.printStackTrace()
            false
        } finally {
            ftpClient.disconnect()
        }
    }
}
