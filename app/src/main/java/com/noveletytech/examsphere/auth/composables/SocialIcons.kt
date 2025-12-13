package com.noveletytech.examsphere.auth.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.noveletytech.examsphere.R

@Composable
fun SocialMediaButtons(onGoogleClick: () -> Unit) {
    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
        // You can add Facebook and Apple buttons here in the same way
        GoogleIcon(onClick = onGoogleClick)
    }
}

@Composable
private fun GoogleIcon(onClick: () -> Unit) {
    Image(
        painter = painterResource(id = R.drawable.ic_google),
        contentDescription = "Google Logo",
        modifier = Modifier
            .size(48.dp)
            .clip(CircleShape)
            .background(Color.White)
            .clickable(onClick = onClick)
            .padding(12.dp)
    )
}