/*
 * Copyright 2021 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.androiddevchallenge.ui

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.androiddevchallenge.ui.theme.MyTheme
import com.example.androiddevchallenge.ui.theme.lightRed
import com.example.androiddevchallenge.ui.theme.red
import com.example.androiddevchallenge.ui.theme.yellow
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
        initialValue = 0.8f, targetValue = 2f,
        animationSpec =
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
