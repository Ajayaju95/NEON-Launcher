package com.neonlauncher.home

import android.app.Activity
import android.content.Intent
import android.content.pm.ResolveInfo
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import android.graphics.Typeface
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.sin

class MainActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        @Suppress("DEPRECATION")
        window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_FULLSCREEN or
            View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or
            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
            View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
        setContentView(NeonHomeView())
    }

    private inner class NeonHomeView : View(this@MainActivity) {
        private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        private val installedApps: List<ResolveInfo> = packageManager.queryIntentActivities(
            Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_LAUNCHER), 0
        ).sortedBy { it.loadLabel(packageManager).toString().lowercase(Locale.getDefault()) }

        private val appNames = listOf(
            "Phone", "Play Store", "Photos", "Gallery", "PenUP",
            "Node Video", "Max VPN", "Chrome", "YouTube", "Netflix", "Settings"
        )
        private val appMarks = listOf("☎", "▶", "✿", "▧", "◉", "N", "M", "◉", "▶", "N", "⚙")
        private val secondPageApps = listOf("Chrome", "Node Video", "Max VPN", "Play Store", "YouTube", "Netflix", "Settings")
        private val secondPageMarks = listOf("◉", "N", "M", "▶", "▶", "N", "⚙")

        private val white = Color.WHITE
        private val secondary = 0xFFB9C1CA.toInt()
        private val dim = 0xFF87929E.toInt()
        private val thin = Typeface.create("sans-serif-light", Typeface.NORMAL)
        private val normal = Typeface.create("sans-serif", Typeface.NORMAL)
        private val medium = Typeface.create("sans-serif-medium", Typeface.NORMAL)

        private var currentPage = 0
        private var listScroll = 0f
        private var downX = 0f
        private var downY = 0f

        override fun onDraw(canvas: Canvas) {
            super.onDraw(canvas)
            canvas.save()
            canvas.scale(width / 432f, height / 936f)
            drawCoastBackground(canvas)
            when (currentPage) {
                0 -> drawAppsScreen(canvas)
                1 -> drawDateScreen(canvas)
                else -> drawClockScreen(canvas)
            }
            canvas.restore()
        }

        private fun drawCoastBackground(c: Canvas) {
            c.drawColor(0xFF080E17.toInt())
            paint.shader = android.graphics.LinearGradient(
                0f, 0f, 432f, 936f,
                intArrayOf(0xFF0B1521.toInt(), 0xFF101923.toInt(), 0xFF080E17.toInt()),
                null, android.graphics.Shader.TileMode.CLAMP
            )
            c.drawRect(0f, 0f, 432f, 936f, paint)
            paint.shader = null

            // A restrained, abstract coastline silhouette in place of the video wallpaper.
            val coast = Path().apply {
                moveTo(287f, -8f)
                cubicTo(254f, 82f, 300f, 140f, 268f, 226f)
                cubicTo(239f, 306f, 278f, 365f, 246f, 456f)
                cubicTo(216f, 550f, 252f, 628f, 219f, 708f)
                cubicTo(193f, 792f, 222f, 858f, 174f, 944f)
                lineTo(440f, 944f)
                lineTo(440f, -8f)
                close()
            }
            paint.color = 0xFF17232F.toInt()
            c.drawPath(coast, paint)

            val shore = Path().apply {
                moveTo(287f, -8f)
                cubicTo(254f, 82f, 300f, 140f, 268f, 226f)
                cubicTo(239f, 306f, 278f, 365f, 246f, 456f)
                cubicTo(216f, 550f, 252f, 628f, 219f, 708f)
                cubicTo(193f, 792f, 222f, 858f, 174f, 944f)
            }
            paint.style = Paint.Style.STROKE
            paint.strokeWidth = 1.4f
            paint.color = 0x887F909D.toInt()
            c.drawPath(shore, paint)
            paint.strokeWidth = 0.65f
            paint.color = 0x667F909D
            for (i in 0..10) {
                val offset = i * 7f
                val wave = Path().apply {
                    moveTo(300f + offset * 0.12f, 20f + i * 72f)
                    cubicTo(280f + offset, 64f + i * 72f, 315f + offset * 0.3f, 87f + i * 72f, 298f + offset * 0.15f, 118f + i * 72f)
                }
                c.drawPath(wave, paint)
            }
            paint.style = Paint.Style.FILL
            paint.color = 0xFF0A111B.toInt()
            c.drawRect(0f, 0f, 205f, 936f, paint)
        }

        private fun drawAppsScreen(c: Canvas) {
            tracked(c, "CUSTOMISE YOUR EXPERIENCE", 43f, 82f, 7f, secondary, medium, 2.0f)
            val weekday = SimpleDateFormat("EEEE", Locale.ENGLISH).format(Date()).uppercase(Locale.ENGLISH)
            tracked(c, "IT'S $weekday", 42f, 127f, 29f, white, thin, 0.2f)
            tracked(c, SimpleDateFormat("dd  MMM  yyyy", Locale.ENGLISH).format(Date()).uppercase(), 44f, 149f, 8f, secondary, normal, 1.0f)
            text(c, "A quieter space for everything you need.", 44f, 179f, 7f, secondary, thin)
            line(c, 44f, 196f, 161f, 196f, 0x667F909D)

            val firstY = 238f - listScroll
            appNames.forEachIndexed { index, name ->
                val y = firstY + index * 49f
                if (y > 205f && y < 890f) {
                    mark(c, appMarks[index], 48f, y - 1f, 17f)
                    text(c, name, 79f, y + 4f, 10f, white, normal)
                }
            }
            tracked(c, "SWIPE  →", 44f, 902f, 7f, dim, medium, 1.5f)
        }

        private fun drawDateScreen(c: Canvas) {
            text(c, SimpleDateFormat("EEE  •  dd MMM", Locale.ENGLISH).format(Date()), 42f, 91f, 8f, secondary, medium)
            text(c, SimpleDateFormat("dd", Locale.ENGLISH).format(Date()), 40f, 176f, 63f, white, thin)
            text(c, "Make space for the things you want to do.", 43f, 207f, 8f, secondary, thin)
            tracked(c, "FIND ANYTHING YOU WANT", 44f, 291f, 6f, secondary, normal, 1.1f)

            paint.style = Paint.Style.STROKE
            paint.strokeWidth = 0.8f
            paint.color = 0x99FFFFFF.toInt()
            c.drawArc(RectF(105f, 300f, 145f, 340f), -80f, 250f, false, paint)
            paint.style = Paint.Style.FILL
            for (i in 0..7) {
                val angle = Math.toRadians((i * 45 - 90).toDouble())
                paint.color = white
                c.drawCircle(125f + 24f * cos(angle).toFloat(), 320f + 24f * sin(angle).toFloat(), 1.1f, paint)
            }

            text(c, SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(Date()), 44f, 421f, 7f, secondary, normal)
            text(c, "Battery level  •  --%", 44f, 442f, 7f, secondary, normal)
            text(c, SimpleDateFormat("EEE, MMM dd", Locale.ENGLISH).format(Date()), 44f, 482f, 9f, white, medium)

            secondPageApps.forEachIndexed { index, name ->
                val y = 530f + index * 49f
                mark(c, secondPageMarks[index], 48f, y - 1f, 19f)
                text(c, name, 83f, y + 4f, 10f, white, normal)
            }
        }

        private fun drawClockScreen(c: Canvas) {
            val cx = 139f
            val cy = 454f
            val radius = 88f
            paint.style = Paint.Style.STROKE
            paint.strokeWidth = 0.8f
            paint.color = 0x557F909D
            c.drawCircle(cx, cy, radius, paint)
            paint.style = Paint.Style.FILL

            for (i in 0 until 60) {
                val angle = Math.toRadians(i * 6.0 - 90.0)
                val inner = radius - if (i % 5 == 0) 9f else 4f
                val x1 = cx + inner * cos(angle).toFloat()
                val y1 = cy + inner * sin(angle).toFloat()
                val x2 = cx + radius * cos(angle).toFloat()
                val y2 = cy + radius * sin(angle).toFloat()
                line(c, x1, y1, x2, y2, if (i % 5 == 0) 0xCCFFFFFF.toInt() else 0x668D98A3)
                if (i % 5 == 0) {
                    val label = String.format(Locale.ENGLISH, "%02d", i)
                    text(c, label, cx + (radius - 21f) * cos(angle).toFloat() - 7f,
                        cy + (radius - 21f) * sin(angle).toFloat() + 3f, 7f, secondary, thin)
                }
            }

            val time = SimpleDateFormat("HH mm", Locale.ENGLISH).format(Date()).split(" ")
            text(c, time[0], 34f, 462f, 39f, white, thin)
            text(c, time[1], 121f, 462f, 17f, white, thin)
            tracked(c, SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH).format(Date()).uppercase(), 254f, 459f, 7f, secondary, medium, 0.8f)
            tracked(c, SimpleDateFormat("EEEE", Locale.ENGLISH).format(Date()).uppercase(), 254f, 477f, 7f, white, medium, 1.0f)
            text(c, "◈  51%", 204f, 888f, 7f, secondary, normal)
        }

        private fun text(c: Canvas, value: String, x: Float, y: Float, size: Float,
                         color: Int = white, face: Typeface = normal) {
            paint.color = color
            paint.textSize = size
            paint.typeface = face
            paint.style = Paint.Style.FILL
            paint.textAlign = Paint.Align.LEFT
            c.drawText(value, x, y, paint)
        }

        private fun tracked(c: Canvas, value: String, x: Float, y: Float, size: Float,
                            color: Int, face: Typeface, spacing: Float) {
            paint.color = color
            paint.textSize = size
            paint.typeface = face
            paint.style = Paint.Style.FILL
            var cursor = x
            value.forEach { character ->
                val part = character.toString()
                c.drawText(part, cursor, y, paint)
                cursor += paint.measureText(part) + spacing
            }
        }

        private fun mark(c: Canvas, value: String, x: Float, y: Float, size: Float) {
            paint.color = white
            paint.textSize = size
            paint.typeface = Typeface.create("sans-serif", Typeface.BOLD)
            paint.textAlign = Paint.Align.CENTER
            c.drawText(value, x, y + 5f, paint)
            paint.textAlign = Paint.Align.LEFT
        }

        private fun line(c: Canvas, x1: Float, y1: Float, x2: Float, y2: Float, color: Int) {
            paint.color = color
            paint.strokeWidth = 0.7f
            paint.style = Paint.Style.STROKE
            c.drawLine(x1, y1, x2, y2, paint)
            paint.style = Paint.Style.FILL
        }

        override fun onTouchEvent(event: MotionEvent): Boolean {
            val sx = width / 432f
            val sy = height / 936f
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    downX = event.x
                    downY = event.y
                    return true
                }
                MotionEvent.ACTION_UP -> {
                    val dx = event.x - downX
                    val dy = event.y - downY
                    if (abs(dx) > width * 0.12f) {
                        currentPage = if (dx < 0) (currentPage + 1) % 3 else (currentPage + 2) % 3
                        invalidate()
                        return true
                    }
                    if (abs(dy) > height * 0.08f) {
                        if (currentPage == 0 && dy < 0) listScroll = (listScroll + 140f).coerceAtMost(260f)
                        else if (currentPage == 0 && dy > 0) listScroll = (listScroll - 140f).coerceAtLeast(0f)
                        invalidate()
                        return true
                    }

                    val x = event.x / sx
                    val y = event.y / sy
                    if (currentPage == 0 && y > 205f) {
                        val index = ((y - (238f - listScroll)) / 49f).toInt()
                        if (index in appNames.indices) launchApp(appNames[index])
                    } else if (currentPage == 1 && y > 505f) {
                        val index = ((y - 530f) / 49f).toInt()
                        if (index in secondPageApps.indices) launchApp(secondPageApps[index])
                    } else if (currentPage == 1 && y in 270f..360f) {
                        Toast.makeText(this@MainActivity, "Swipe to the app list to open apps", Toast.LENGTH_SHORT).show()
                    }
                    return true
                }
            }
            return true
        }

        private fun launchApp(label: String) {
            val target = installedApps.firstOrNull {
                it.loadLabel(packageManager).toString().equals(label, true)
            } ?: installedApps.firstOrNull {
                it.loadLabel(packageManager).toString().contains(label, true)
            }
            if (target == null) {
                Toast.makeText(this@MainActivity, "$label is not installed", Toast.LENGTH_SHORT).show()
                return
            }
            val intent = Intent(Intent.ACTION_MAIN)
                .addCategory(Intent.CATEGORY_LAUNCHER)
                .setClassName(target.activityInfo.packageName, target.activityInfo.name)
            try {
                startActivity(intent)
            } catch (_: Exception) {
                Toast.makeText(this@MainActivity, "Unable to open $label", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
