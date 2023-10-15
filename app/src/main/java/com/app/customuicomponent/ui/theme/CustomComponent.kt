package com.app.customuicomponent.ui.theme

import androidx.compose.animation.Animatable
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.*
import androidx.compose.ui.geometry.*

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit


@Composable
fun CustomComponent(
    canvasSize: Dp = 300.dp,
    indicatorValue: Int = 0, // -> indicator that will change based on number input
    maxIndicatorValue: Int = 100, // -> the max of indicator
    backgroundIndicatorColor: Color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f),
    backgroundIndicatorStrokeWidth: Float = 100f,
    foregroundIndicatorColor: Color = MaterialTheme.colorScheme.primary,
    foregroundIndicatorStrokeWidth: Float = 100f,

    // EmbeddedElement
    bigTextFontSize: TextUnit = MaterialTheme.typography.displaySmall.fontSize,
    bigTextColor: Color = MaterialTheme.colorScheme.onSurface,
    bigTextSuffix: String = "GB",
    smallTextColor: Color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f),
    smallText: String = "Remaining",
    smallTextFontSize: TextUnit = MaterialTheme.typography.titleLarge.fontSize


){

    var allowedIndicatorValue by remember { mutableStateOf(maxIndicatorValue) }

    allowedIndicatorValue = if (indicatorValue <= maxIndicatorValue){
        indicatorValue
    } else {
        maxIndicatorValue
    }

    var animatedIndicatorValue by remember { mutableStateOf(0f) }

    LaunchedEffect(key1 = allowedIndicatorValue ){
        animatedIndicatorValue = allowedIndicatorValue.toFloat()
    }

    /* the formula to have percentage of maxValue */
    // example = 25(indicatorValue) / 100 (maxIndicatorValue) * 100 = 25 -> the value will follow the maxIndicatorValue
    // example2 = 25/1000 * 100 = 2.5 -> will animation will have 2.5 percent of maxValue(max animation)
    val percentage = (animatedIndicatorValue / maxIndicatorValue) * 100

    val animatedSweepAngle by animateFloatAsState(
        targetValue = (2.4 * percentage).toFloat(),
        animationSpec = tween(1000),
        label = "AnimatedSweepAngle"
    )

    val receivedValue by animateIntAsState(
        targetValue = allowedIndicatorValue,
        animationSpec = tween(1000),
        label = "ReceivedValue")

    val animatedBigTextColor by animateColorAsState(
        targetValue = if (allowedIndicatorValue == 0)
            MaterialTheme.colorScheme.onSurface.copy(0.3f)
        else
            bigTextColor,
        label = "AnimatedBigTextColor",
        animationSpec = tween(1000)
    )

    Column (
        modifier = Modifier
            .size(canvasSize)
            .drawBehind {// -> is the custom component that draw on top of canvas
                val componentSize = size / 1.25f // -> custom component size (canvas size / 1.25f)
                backgroundIndicator(
                    componentSize = componentSize,
                    indicatorColor = backgroundIndicatorColor,
                    indicatorStrokeWidth = backgroundIndicatorStrokeWidth
                )

                foregroundIndicator(
                    sweepAngle = animatedSweepAngle,
                    componentSize = componentSize,
                    indicatorColor = foregroundIndicatorColor,
                    indicatorStrokeWidth = foregroundIndicatorStrokeWidth
                )
            },
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        EmbeddedElement(
            bigText = receivedValue,
            bigTextFontSize = bigTextFontSize,
            bigTextColor = animatedBigTextColor,
            bigTextSuffix = bigTextSuffix,
            smallText = smallText,
            smallTextColor = smallTextColor,
            smallTextFontSize = smallTextFontSize
        )
    }
}


fun DrawScope.backgroundIndicator(
    componentSize: Size,
    indicatorColor: Color,
    indicatorStrokeWidth: Float
){          // -> the part that not animated (background of animation)
    drawArc(
        size = componentSize,
        color =  indicatorColor,
        startAngle = 150f,
        sweepAngle = 240f,
        useCenter = false,
        style = Stroke(
            width = indicatorStrokeWidth,
            cap = StrokeCap.Round
        ),
        topLeft = Offset( // -> topLeft is the place that the formula will be adopted
            x = (size.width - componentSize.width) / 2f, // -> size is the canvas size
            y = (size.height - componentSize.height) / 2f
        )
    )
}

fun DrawScope.foregroundIndicator(
    sweepAngle: Float,
    componentSize: Size,
    indicatorColor: Color,
    indicatorStrokeWidth: Float
){          // -> the part that not animated (background of animation)
    drawArc(
        size = componentSize,
        color =  indicatorColor,
        startAngle = 150f,
        sweepAngle = sweepAngle,
        useCenter = false,
        style = Stroke(
            width = indicatorStrokeWidth,
            cap = StrokeCap.Round
        ),
        topLeft = Offset( // -> topLeft is the place that the formula will be adopted
            x = (size.width - componentSize.width) / 2f, // -> size is the canvas size
            y = (size.height - componentSize.height) / 2f
        )
    )
}

@Composable
fun EmbeddedElement(
    bigText: Int,
    bigTextFontSize: TextUnit,
    bigTextColor: Color,
    bigTextSuffix: String,
    smallText: String,
    smallTextColor: Color,
    smallTextFontSize: TextUnit
){
    Text(
        text = smallText,
        color = smallTextColor,
        fontSize = smallTextFontSize,
        textAlign = TextAlign.Center
    )

    Text(
        text = "$bigText ${bigTextSuffix.take(2)}",
        color = bigTextColor,
        fontSize = bigTextFontSize,
        textAlign = TextAlign.Center,
        fontWeight = FontWeight.Bold
    )
}

@Composable
@Preview(showBackground = true)
fun CustomComponentPreview(){
    CustomComponent()
}


// Documentation
/*Kode di atas adalah implementasi dari suatu komponen UI kustom yang menggunakan Jetpack Compose. Fungsi `CustomComponent` menggambar sebuah indikator animasi di atas kanvas. Saat terjadi recompose (pembaruan tampilan), berikut adalah urutan jalannya kode:

1. **Inisialisasi Variabel dan Pengaturan Awal**

   - `CustomComponent` dijalankan dan mengambil beberapa parameter sebagai masukan.
   - `allowedIndicatorValue` dan `animatedIndicatorValue` diinisialisasi dengan nilai awal sesuai dengan parameter yang diberikan.
   - `allowedIndicatorValue` diperbarui berdasarkan apakah `indicatorValue` kurang dari atau sama dengan `maxIndicatorValue`.
   - `animatedIndicatorValue` diperbarui dengan nilai dari `allowedIndicatorValue`.

2. **Perhitungan Persentase dan Animasi**

   - `percentage` dihitung berdasarkan `animatedIndicatorValue` dan `maxIndicatorValue`.
   - `animatedSweepAngle` dihitung sebagai hasil animasi dari persentase.

3. **Pembangunan Tampilan**

   - `Column` digunakan untuk mengatur tata letak komponen.
   - Di dalam `Column`, terdapat dua fungsi `drawBehind` yang menggambar latar belakang dan depan dari indikator.

4. **Penggambaran Latar Belakang dan Depan**

   - `backgroundIndicator` dan `foregroundIndicator` adalah dua fungsi yang digunakan untuk menggambar latar belakang dan depan dari indikator, masing-masing.

5. **Recompose dan Pembaruan Tampilan**

   - Saat terjadi perubahan pada properti `indicatorValue` atau jika ada perintah recompose, kode di dalam `CustomComponent` akan dijalankan kembali.
   - Kode ini akan memperbarui nilai `allowedIndicatorValue` berdasarkan `indicatorValue` dan `maxIndicatorValue`.
   - `animatedIndicatorValue` diperbarui sesuai dengan `allowedIndicatorValue`.
   - Persentase dan sudut sapuan animasi akan dihitung ulang.

6. **Animasi dan Tampilan yang Diperbarui**

   - `animatedSweepAngle` akan mendapatkan nilai baru berdasarkan persentase yang dihasilkan dari `animatedIndicatorValue`.

7. **Penggambaran Ulang**

   - Karena ada perubahan dalam tampilan atau animasi, Jetpack Compose akan secara otomatis menggambar ulang komponen.

Kesimpulannya, setiap kali terjadi recompose, properti `indicatorValue` akan diperbarui, dan komponen akan dijalankan ulang. Hal ini akan memicu animasi dan pembaruan tampilan yang sesuai dengan nilai baru dari `indicatorValue`.*/