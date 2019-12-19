package fabriciocarvalhal.com.br.dogbreedfinder.helpers

import android.graphics.*
import com.squareup.picasso.Transformation

class ImageRoundCorners:Transformation {

    override fun transform(source: Bitmap):Bitmap {
        val output = Bitmap.createBitmap(source.getWidth(), source
            .getHeight(), Bitmap.Config.ARGB_8888)
        val canvas = Canvas(output)
        val color = -0xbdbdbe
        val paint = Paint()
        val rect = Rect(0, 0, source.getWidth(), source.getHeight())
        val rectF = RectF(rect)
        val roundPx = 50f
        paint.setAntiAlias(true)
        canvas.drawARGB(0, 0, 0, 0)
        paint.setColor(color)
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint)
        paint.setXfermode(PorterDuffXfermode(PorterDuff.Mode.SRC_IN))
        canvas.drawBitmap(source, rect, rect, paint)
        source.recycle();
        return output
    }

    override fun key():String {
        return "RoundImage"
    }
}