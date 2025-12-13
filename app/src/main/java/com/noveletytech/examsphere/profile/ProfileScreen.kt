package com.noveletytech.examsphere.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.google.firebase.auth.FirebaseAuth
import com.noveletytech.examsphere.home.HomeViewModel

@Composable
fun ProfileScreen(
    onSignOut: () -> Unit,
    viewModel: HomeViewModel
) {
    val currentUser by viewModel.currentUser.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (currentUser?.profilePictureUrl?.isNotEmpty() == true) {
            AsyncImage(
                model = currentUser?.profilePictureUrl,
                contentDescription = "Profile Picture",
                modifier = Modifier
                    .size(128.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
        } else {
            Icon(
                imageVector = Icons.Default.AccountCircle,
                contentDescription = "User Avatar",
                modifier = Modifier.size(128.dp)
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text("Welcome, ${currentUser?.firstName ?: "User"}")
        Text(text = currentUser?.email ?: "")
        Spacer(modifier = Modifier.height(32.dp))
        Button(onClick = {
            FirebaseAuth.getInstance().signOut()
            onSignOut()
        }) {
            Text("Sign Out")
        }
    }
}