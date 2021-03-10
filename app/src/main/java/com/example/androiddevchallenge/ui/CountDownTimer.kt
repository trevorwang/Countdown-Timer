package com.example.androiddevchallenge.ui

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.androiddevchallenge.ui.theme.*
import java.time.format.TextStyle
import kotlin.math.roundToInt

@Composable
fun CountDownTimer(timeInSec: Int) {
    Surface(color = red, modifier = Modifier.fillMaxSize()) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.padding(40.dp)
        ) {
            CountdownCircle(timeInSec)
            TimerText(timeInSec)
        }
    }
}

@Composable
fun TimerText(timeInSec: Int) {
    var trigger by remember { mutableStateOf(timeInSec) }
    val animateTween by animateFloatAsState(
        targetValue = trigger.toFloat(),
        animationSpec = tween(
            durationMillis = timeInSec * 1000,
            easing = LinearEasing
        ),
        finishedListener = {
        }
    )
    DisposableEffect(Unit) {
        trigger = 0
        onDispose { }
    }


    val transition = rememberInfiniteTransition()
    val fontScale by transition.animateFloat(
        initialValue = 0.8f, targetValue = 2f, animationSpec =
        infiniteRepeatable(tween(500), RepeatMode.Reverse)
    )
    format(timeInSec = animateTween.roundToInt(), fontScale = fontScale)
}

@Composable
fun DisplayTime(time: String, indicator: String = "", fontScale: Float = 1f) {
    Text(
        text = "$time$indicator",
        fontSize = 64.sp * fontScale,
        color = Color.White,
        fontFamily = FontFamily.Monospace,
        fontWeight = FontWeight.W700,
    )
}

@Composable
fun format(timeInSec: Int, fontScale: Float = 1f) {
    val min = timeInSec / 60
    val sec = timeInSec % 60
    Row {
        if (min > 0) {
            DisplayTime(time = min.formatTime())
        }

        val text = if (min == 0) sec.toString() else sec.formatTime()
        val scale = if (sec > 10 || sec == 0) 1f else fontScale
        DisplayTime(time = text, fontScale = scale)
    }
}

private fun Int.formatTime() = String.format("%02d", this)

@Composable
fun CountdownCircle(timeInSec: Int, started: Boolean = true) {
    val strokeWidth = 80.dp.value
    var trigger by remember { mutableStateOf(timeInSec) }
    val animateTween by animateFloatAsState(
        targetValue = trigger.toFloat(),
        animationSpec = tween(
            durationMillis = timeInSec * 1000,
            easing = LinearEasing
        ),
        finishedListener = {
        }
    )

    DisposableEffect(Unit) {
        trigger = 0
        onDispose { }
    }

    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .padding(20.dp)
    ) {


        drawCircle(
            color = lightRed,
            style = Stroke(width = strokeWidth)
        )

        drawCircle(
            color = yellow,
            radius = size.minDimension / 2.0f - strokeWidth / 2,
        )


        val color = Color.White
        drawArc(
            color = color,
            startAngle = 270f,
            sweepAngle = animateTween * 360f / timeInSec,
            useCenter = false,
            style = Stroke(width = strokeWidth),
        )
    }
}

@Composable
@Preview("CountDownTimer", widthDp = 360, heightDp = 640)
fun DefaultPreview() {
    MyTheme {
        CountDownTimer(20)
    }
}