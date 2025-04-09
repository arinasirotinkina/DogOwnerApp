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
    suspend fun upaloadFileToFTP(uri: Uri, context: Context) = withContext(Dispatchers.IO) {
        val ftpClient = FTPClient()

        try {
            ftpClient.connect("arinas8t.beget.tech", 21) // Указываем порт FTP (обычно 21)
            ftpClient.setConnectTimeout(5000) // Тайм-аут подключения
            ftpClient.setDataTimeout(5000)    // Тайм-аут передачи данных

            // Переход в пассивный режим
            ftpClient.enterLocalPassiveMode()
            Log.d("FTP", "Подключение успешно!")

            val loginSuccess = ftpClient.login("arinas8t_h", "H123h456!")
            if (!loginSuccess) {
                Log.e("FTP", "Ошибка аутентификации")
                return@withContext
            }
            Log.d("FTP", "Авторизация успешна!")

            ftpClient.setFileType(FTP.BINARY_FILE_TYPE)
            Log.d("FTP", "Тип файла установлен!")

            // Открываем поток для файла
            val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
            if (inputStream == null) {
                Log.e("FTP", "Не удалось открыть поток для $uri")
                return@withContext
            }

            val remoteFileName = "arinas8t.beget.tech/public_html/photo/users/${"dd"}.jpg" // Уникальное имя
            val uploadSuccess = ftpClient.storeFile(remoteFileName, inputStream)

            if (uploadSuccess) {
                Log.d("FTP", "Файл успешно загружен: $remoteFileName")
            } else {
                Log.e("FTP", "Ошибка при загрузке файла.")
            }

            // Закрываем соединение
            inputStream.close()
            ftpClient.logout()
            ftpClient.disconnect()
        } catch (e: IOException) {
            e.printStackTrace()
            Log.e("FTP", "Ошибка: ${e.message}")
        }
    }
    suspend fun uploadFileToFTP(uri: Uri, context: Context, collection: String, id: String) = withContext(Dispatchers.IO) {
        val ftpClient = FTPClient()

        try {
            ftpClient.connect("arinas8t.beget.tech", 21) // Указываем порт FTP (обычно 21)
            ftpClient.setConnectTimeout(5000) // Тайм-аут подключения
            ftpClient.setDataTimeout(5000)    // Тайм-аут передачи данных

            // Переход в пассивный режим
            ftpClient.enterLocalPassiveMode()
            Log.d("FTP", "Подключение успешно!")

            val loginSuccess = ftpClient.login("arinas8t_h", "H123h456!")
            if (!loginSuccess) {
                Log.e("FTP", "Ошибка аутентификации")
                return@withContext
            }
            Log.d("FTP", "Авторизация успешна!")

            ftpClient.setFileType(FTP.BINARY_FILE_TYPE)
            Log.d("FTP", "Тип файла установлен!")

            // Открываем поток для файла
            val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
            if (inputStream == null) {
                Log.e("FTP", "Не удалось открыть поток для $uri")
                return@withContext
            }

            val remoteFileName = "arinas8t.beget.tech/public_html/photo/$collection/$id" // Уникальное имя
            val uploadSuccess = ftpClient.storeFile(remoteFileName, inputStream)

            if (uploadSuccess) {
                Log.d("FTP", "Файл успешно загружен: $remoteFileName")
            } else {
                Log.e("FTP", "Ошибка при загрузке файла.")
            }

            // Закрываем соединение
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
            ftpClient.connect("arinas8t.beget.tech", 21) // Указываем порт FTP (обычно 21)
             ftpClient.connectTimeout = 2000 // Тайм-аут подключения
            ftpClient.setDataTimeout(2000)    // Тайм-аут передачи данных

            Log.d("FTP", "Подключение успешно!")

            ftpClient.login("arinas8t_h", "H123h456!")

            ftpClient.enterLocalPassiveMode() // Включаем пассивный режим (важно для Android)
             val changeDirSuccess = ftpClient.changeWorkingDirectory("arinas8t.beget.tech/public_html/photo/$collection")
             if (changeDirSuccess) {
                 Log.d("FTP", "Директория изменена на: $collection")
             } else {
                 Log.d("FTP", "Не удалось сменить директорию.")
                 return@withContext false
             }

             // Переименовываем файл
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
            ftpClient.logout() // Выход
            success // Возвращаем true, если успешно
        } catch (e: IOException) {
            e.printStackTrace()
            false // Возвращаем false в случае ошибки
        } finally {
            ftpClient.disconnect()
        }
    }
}
