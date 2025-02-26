package com.madhuresh.supermarket.bottomNavScreens

import android.app.Activity
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.madhuresh.supermarket.dataModel.User
import com.madhuresh.supermarket.ui.theme.SuperMarketTheme
import com.madhuresh.supermarket.viewModel.AuthViewModel

@Composable
fun AccountScreen(navController: NavController, authViewModel: AuthViewModel = viewModel()) {
    val isAuthenticated by authViewModel.isAuthenticated.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        val window = (context as? Activity)?.window
        val statusBarColor = Color(78, 177, 118).toArgb()
        window?.statusBarColor = statusBarColor
    }

    SuperMarketTheme {
        Surface(
            color = if (isSystemInDarkTheme()) Color.Black else Color.White,
            modifier = Modifier.fillMaxSize()
        ) {
            if (isAuthenticated) {
                val user by authViewModel.user.collectAsState()
                user?.let {
                    AuthenticatedUserContent(
                        user = it,
                        onSignOutClicked = {
                            authViewModel.signOut(context)
                        },
                        navController = navController
                    )
                }
            } else {
                UnauthenticatedUserContent(
                    onSignIn = { email, password ->
                        authViewModel.signIn(email, password, context)
                    },
                    onRegister = { name, email, password, phone, address ->
                        authViewModel.register(name, email, password, phone, address, context)
                    }
                )
            }
        }
    }
}


@Composable
fun AuthenticatedUserContent(user: User, onSignOutClicked: () -> Unit, navController: NavController) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(if (isSystemInDarkTheme()) Color(78, 177, 118) else Color(78, 177, 118))
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
        ) {
            Row(
                modifier = Modifier
                    .padding(start = 8.dp, top = 8.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Start
            ) {
                Text(
                    text = "Welcome, ",
                    fontWeight = FontWeight.Normal,
                    fontSize = 22.sp,
                    color = if (isSystemInDarkTheme()) Color.White else Color.White,
                    modifier = Modifier.padding(top = 2.dp)
                )
                Text(
                    text = user.name,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 28.sp,
                    color = if (isSystemInDarkTheme()) Color.White else Color.White
                )
            }

            Spacer(modifier = Modifier.padding(8.dp))

            Button(
                onClick = {
                    navController.navigate("myProfile")
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isSystemInDarkTheme()) Color.White else Color.White
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(70.dp)
                    .padding(8.dp)
                    .clip(RoundedCornerShape(8.dp))
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        Icons.Default.AccountCircle,
                        contentDescription = null,
                        tint = if (isSystemInDarkTheme()) Color(78, 177, 118) else Color(78, 177, 118),
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "My Profile",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 18.sp,
                        color = if (isSystemInDarkTheme()) Color(78, 177, 118) else Color(78, 177, 118),
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            Button(
                onClick = {
                    navController.navigate("order")
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isSystemInDarkTheme()) Color.White else Color.White
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(70.dp)
                    .padding(8.dp)
                    .clip(RoundedCornerShape(8.dp))
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        Icons.Default.ShoppingCart,
                        contentDescription = null,
                        tint = if (isSystemInDarkTheme()) Color(78, 177, 118) else Color(78, 177, 118),
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Orders",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 18.sp,
                        color = if (isSystemInDarkTheme()) Color(78, 177, 118) else Color(78, 177, 118),
                        modifier = Modifier.weight(1f)
                    )
                }
            }

//            Button(
//                onClick = {
//                    navController.navigate("setting")
//                },
//                colors = ButtonDefaults.buttonColors(
//                    containerColor = if (isSystemInDarkTheme()) Color.White else Color.White
//                ),
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .height(70.dp)
//                    .padding(8.dp)
//                    .clip(RoundedCornerShape(8.dp))
//            ) {
//                Row(
//                    verticalAlignment = Alignment.CenterVertically,
//                    horizontalArrangement = Arrangement.Start,
//                    modifier = Modifier.fillMaxWidth()
//                ) {
//                    Icon(
//                        Icons.Default.Settings,
//                        contentDescription = null,
//                        tint = if (isSystemInDarkTheme()) Color(78, 177, 118) else Color(78, 177, 118),
//                        modifier = Modifier.size(24.dp)
//                    )
//                    Spacer(modifier = Modifier.width(8.dp))
//                    Text(
//                        text = "Settings",
//                        fontWeight = FontWeight.SemiBold,
//                        fontSize = 18.sp,
//                        color = if (isSystemInDarkTheme()) Color(78, 177, 118) else Color(78, 177, 118),
//                        modifier = Modifier.weight(1f)
//                    )
//                }
//            }

            Button(
                onClick = {
                    navController.navigate("help")
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isSystemInDarkTheme()) Color.White else Color.White
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(70.dp)
                    .padding(8.dp)
                    .clip(RoundedCornerShape(8.dp))
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        Icons.Default.Email,
                        contentDescription = null,
                        tint = if (isSystemInDarkTheme()) Color(78, 177, 118) else Color(78, 177, 118),
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Help",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 18.sp,
                        color = if (isSystemInDarkTheme()) Color(78, 177, 118) else Color(78, 177, 118),
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            Button(
                onClick = {
                    navController.navigate("about")
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isSystemInDarkTheme()) Color.White else Color.White
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(70.dp)
                    .padding(8.dp)
                    .clip(RoundedCornerShape(8.dp))
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        Icons.Default.Info,
                        contentDescription = null,
                        tint = if (isSystemInDarkTheme()) Color(78, 177, 118) else Color(78, 177, 118),
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "About",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 18.sp,
                        color = if (isSystemInDarkTheme()) Color(78, 177, 118) else Color(78, 177, 118),
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            Button(
                onClick = {
                    navController.navigate("privacy")
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isSystemInDarkTheme()) Color.White else Color.White
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(70.dp)
                    .padding(8.dp)
                    .clip(RoundedCornerShape(8.dp))
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        Icons.Default.Lock,
                        contentDescription = null,
                        tint = if (isSystemInDarkTheme()) Color(78, 177, 118) else Color(78, 177, 118),
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Privacy Policy",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 18.sp,
                        color = if (isSystemInDarkTheme()) Color(78, 177, 118) else Color(78, 177, 118),
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))
        }

        Button(
            onClick = {
                onSignOutClicked()
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = if (isSystemInDarkTheme()) Color(243, 243, 243) else Color(243, 243, 243)
            ),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .height(70.dp)
                .padding(8.dp)
                .clip(RoundedCornerShape(8.dp))
        ) {
            Text(
                text = "Sign Out",
                fontWeight = FontWeight.SemiBold,
                fontSize = 20.sp,
                color = if (isSystemInDarkTheme()) Color(78, 177, 118) else Color(78, 177, 118)
            )
        }
    }
}

@Composable
fun UnauthenticatedUserContent(onSignIn: (String, String) -> Unit, onRegister: (String, String, String, String, String) -> Unit) {

    var isRegistering by remember { mutableStateOf(true) }

    SuperMarketTheme {
        Surface(
            color = if (isSystemInDarkTheme()) Color.Black else Color.White,
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                if (isRegistering) {
                    RegistrationForm(onRegister = onRegister, onSwitchToSignIn = { isRegistering = false })
                } else {
                    SignInForm(onSignIn = onSignIn, onSwitchToRegister = { isRegistering = true })
                }
            }
        }
    }
}

@Composable
fun SignInForm(onSignIn: (String, String) -> Unit, onSwitchToRegister: () -> Unit) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val context = LocalContext.current

    fun validateInputs(): Boolean {
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(context, "Fields cannot be empty", Toast.LENGTH_SHORT).show()
            return false
        }

        if (!email.endsWith("@gmail.com")) {
            Toast.makeText(context, "Email must end with @gmail.com", Toast.LENGTH_SHORT).show()
            return false
        }

        if(password.length < 8){
            Toast.makeText(context, "Password must be of 8 characters", Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .background(if (isSystemInDarkTheme()) Color(78, 177, 118) else Color(78, 177, 118))
    ) {
        Text(
            text = "Sign In",
            fontSize = 20.sp,
            color = if (isSystemInDarkTheme()) Color.White else Color.White,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(top = 16.dp)
        )
        Spacer(modifier = Modifier.padding(5.dp))
        Text(
            text = "A whole grocery store at\n your fingertips",
            fontSize = 15.sp,
            color = if (isSystemInDarkTheme()) Color.White else Color.White,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center
        )
        Card(
            shape = RoundedCornerShape(15.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Max)
                .padding(16.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .background(if (isSystemInDarkTheme()) Color.White else Color.White)

            ) {
                RegisterTextField(
                    value = email,
                    onValueChange = { email = it },
                    "Email",
                    {Icon(imageVector = Icons.Default.Email, contentDescription = null)}
                )
                RegisterTextField(
                    value = password,
                    onValueChange = { password = it },
                    "Password",
                    {Icon(imageVector = Icons.Default.Lock, contentDescription = null)},
                    visualTransformation = PasswordVisualTransformation(),
                )
                Button(
                    onClick = {
                        if (validateInputs()) {
                            onSignIn(email, password)
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isSystemInDarkTheme()) Color(78, 177, 118) else Color(78, 177, 118)
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(70.dp)
                        .padding(8.dp)
                        .clip(RoundedCornerShape(8.dp))
                ) {
                    Text(
                        text = "Sign In",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 20.sp,
                        color = if (isSystemInDarkTheme()) Color.White else Color.White
                    )
                }
                TextButton(onClick = onSwitchToRegister) {
                    Text(
                        text = "Do not have an account?\nSign Up",
                        fontSize = 15.sp,
                        color = if (isSystemInDarkTheme()) Color(78, 177, 118) else Color(78, 177, 118),
                        fontWeight = FontWeight.Medium,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

@Composable
fun RegistrationForm(onRegister: (String, String, String, String, String) -> Unit, onSwitchToSignIn: () -> Unit) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    val context = LocalContext.current

    fun validateInputs(): Boolean {
        if (name.isEmpty() || email.isEmpty() || password.isEmpty() || phone.isEmpty() || address.isEmpty()) {
            Toast.makeText(context, "Fields cannot be empty", Toast.LENGTH_SHORT).show()
            return false
        }

        val nameRegex = Regex("^[A-Za-z ]+$")
        if (!nameRegex.matches(name)) {
            Toast.makeText(context, "Name should only contain letters and spaces", Toast.LENGTH_SHORT).show()
            return false
        }

        if (!email.endsWith("@gmail.com")) {
            Toast.makeText(context, "Email must end with @gmail.com", Toast.LENGTH_SHORT).show()
            return false
        }

        if(password.length < 8){
            Toast.makeText(context, "Password must be of 8 characters", Toast.LENGTH_SHORT).show()
            return false
        }

        val phoneRegex = Regex("^\\d{10}$")
        if (!phoneRegex.matches(phone)) {
            Toast.makeText(context, "Phone must contain exactly 10 digits", Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .background(if (isSystemInDarkTheme()) Color(78, 177, 118) else Color(78, 177, 118))
    ) {
        Text(
            text = "Sign Up",
            fontSize = 20.sp,
            color = if (isSystemInDarkTheme()) Color.White else Color.White,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(top = 16.dp)
        )
        Spacer(modifier = Modifier.padding(5.dp))
        Text(
            text = "A whole grocery store at\n your fingertips",
            fontSize = 15.sp,
            color = if (isSystemInDarkTheme()) Color.White else Color.White,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center
        )
        Card(
            shape = RoundedCornerShape(15.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Max)
                .padding(16.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .background(if (isSystemInDarkTheme()) Color.White else Color.White)

            ) {
                RegisterTextField(
                    value = name,
                    onValueChange = { name = it },
                    "Name",
                    {Icon(imageVector = Icons.Default.Person, contentDescription = null)}
                )
                RegisterTextField(
                    value = email,
                    onValueChange = { email = it },
                    "Email",
                    {Icon(imageVector = Icons.Default.Email, contentDescription = null)}
                )
                RegisterTextField(
                    value = password,
                    onValueChange = { password = it },
                    "Password",
                    {Icon(imageVector = Icons.Default.Lock, contentDescription = null)},
                    visualTransformation = PasswordVisualTransformation(),
                )
                RegisterTextField(
                    value = phone,
                    onValueChange = { phone = it },
                    "Phone",
                    {Icon(imageVector = Icons.Default.Phone, contentDescription = null)}
                )
                RegisterTextField(
                    value = address,
                    onValueChange = { address = it },
                    "Address",
                    {Icon(imageVector = Icons.Default.LocationOn, contentDescription = null)}
                )
                Button(
                    onClick = {
                        if (validateInputs()) {
                            onRegister(name, email, password, phone, address)
                            onSwitchToSignIn()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isSystemInDarkTheme()) Color(78, 177, 118) else Color(78, 177, 118)
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(70.dp)
                        .padding(8.dp)
                        .clip(RoundedCornerShape(8.dp))
                ) {
                    Text(
                        text = "Sign Up",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 20.sp,
                        color = if (isSystemInDarkTheme()) Color.White else Color.White
                    )
                }
                TextButton(onClick = onSwitchToSignIn) {
                    Text(
                        text = "Already have an account?\nSign In",
                        fontSize = 15.sp,
                        color = if (isSystemInDarkTheme()) Color(78, 177, 118) else Color(78, 177, 118),
                        fontWeight = FontWeight.Medium,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

@Composable
fun RegisterTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    leadingIcon: @Composable (() -> Unit)? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None
) {
    TextField(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Max)
            .padding(10.dp),
        shape = RoundedCornerShape(30.dp),
        value = value,
        onValueChange = onValueChange,
        visualTransformation = visualTransformation,
        placeholder = { Text(text = placeholder) },
        leadingIcon = leadingIcon,
        singleLine = true,
        maxLines = 1,
        colors = TextFieldDefaults.colors(
            focusedTextColor = if (isSystemInDarkTheme()) Color.Black else Color.Black,
            unfocusedTextColor = if (isSystemInDarkTheme()) Color.Black else Color.Black,
            unfocusedLeadingIconColor = if (isSystemInDarkTheme()) Color.Black else Color.Black,
            focusedLeadingIconColor = if (isSystemInDarkTheme()) Color.Black else Color.Black,
            unfocusedPlaceholderColor = if (isSystemInDarkTheme()) Color.Black else Color.Black,
            focusedPlaceholderColor = if (isSystemInDarkTheme()) Color.Black else Color.Black,
            unfocusedContainerColor = if (isSystemInDarkTheme()) Color(243, 243, 243) else Color(243, 243, 243),
            focusedContainerColor = if (isSystemInDarkTheme()) Color(243, 243, 243) else Color(243, 243, 243),
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        )
    )
}
