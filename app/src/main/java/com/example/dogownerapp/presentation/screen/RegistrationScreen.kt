import android.app.Activity
import android.content.Intent
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.startActivity
import com.example.dogownerapp.MainActivity
import com.example.dogownerapp.R
import com.example.dogownerapp.domain.model.AuthResult
import com.example.dogownerapp.presentation.auth.RegisterActivity
import com.example.dogownerapp.presentation.auth.RegisterViewModel


@Composable
fun RegistrationScreen( viewModel: RegisterViewModel) {
    val state by viewModel.authResult.collectAsState()
    var email by remember { mutableStateOf("") }
    val activity = LocalActivity.current
    var password by remember { mutableStateOf("") }
    MaterialTheme(colorScheme = customColors) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(R.drawable.start_image),
                contentDescription = "Моё изображение",
                modifier = Modifier
                    .size(200.dp) // Задаем размер (ширина и высота одинаковые)
                    .align(Alignment.CenterHorizontally) // Выравнивание по горизонтали
            )
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = stringResource(R.string.app_name),
                color = customColors.primary,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier.height(16.dp))
            // Поле для ввода email
            TextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = customColors.background, // Фон при фокусе
                    unfocusedContainerColor = customColors.background, // Фон без фокуса
                    disabledContainerColor = customColors.background // Фон в отключенном состоянии
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            TextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                visualTransformation = PasswordVisualTransformation(),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = customColors.background, // Фон при фокусе
                    unfocusedContainerColor = customColors.background, // Фон без фокуса
                    disabledContainerColor = customColors.background // Фон в отключенном состоянии
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Кнопка регистрации
            Button(
                onClick = { viewModel.register(email, password) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Зарегистрироваться")
            }

            Spacer(modifier = Modifier.height(36.dp))

            // Показать ошибку, если она есть
            if (state is AuthResult.Error) {
                Text(text = (state as AuthResult.Error).message, color = Color.Red)
            }
            if (state is AuthResult.Success) {
                val intent = Intent(activity, MainActivity::class.java)
                activity?.startActivity(intent)
                activity?.finish()
            }

        }
    }
}

val customColors = lightColorScheme(
    primary = Color(0xFFA74B0F),
    secondary = Color(0xFFBE4D20),
    background = Color(0xFFFDF0E9),
    surface = Color(0xFFFACFB2),
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black
)

@Preview(showBackground = true)
@Composable
fun PreviewRegistrationScreen() {
    MaterialTheme(colorScheme = customColors) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(R.drawable.start_image),
                contentDescription = "Моё изображение",
                modifier = Modifier
                    .size(200.dp) // Задаем размер (ширина и высота одинаковые)
                    .align(Alignment.CenterHorizontally) // Выравнивание по горизонтали
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = stringResource(R.string.app_name),
                color = customColors.primary,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier.height(16.dp))
            // Поле для ввода email
            TextField(
                value = "email",
                onValueChange = {   },
                label = { Text("Email") },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = customColors.background, // Фон при фокусе
                    unfocusedContainerColor = customColors.background, // Фон без фокуса
                    disabledContainerColor = Color.Gray // Фон в отключенном состоянии
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Поле для ввода пароля
            TextField(
                value = "password",
                onValueChange = {},
                label = { Text("Password") },
                visualTransformation = PasswordVisualTransformation(),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = customColors.background, // Фон при фокусе
                    unfocusedContainerColor = customColors.background, // Фон без фокуса
                    disabledContainerColor = Color.Gray // Фон в отключенном состоянии
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Кнопка регистрации
            Button(
                onClick = {  },
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text("Зарегистрироваться")
            }
            Spacer(modifier = Modifier.height(36.dp))


            // Показать ошибку, если она есть


        }
    }

}

