package Main;

import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

public class TransformUtils {
	public static BufferedImage rotate(BufferedImage img, int cdir, int pdir) {
		AffineTransform transform = new AffineTransform();
		transform.rotate(Math.toRadians(-(cdir - pdir) * 90), img.getWidth() / 2, img.getHeight() / 2);
		AffineTransformOp op = new AffineTransformOp(transform, AffineTransformOp.TYPE_BILINEAR);
		img = op.filter(img, null);

		return img;
	}

	public static BufferedImage scale(BufferedImage img, double scale) {
		int w = img.getWidth();
		int h = img.getHeight();
		BufferedImage after = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		AffineTransform at = new AffineTransform();
		at.scale(scale, scale);
		AffineTransformOp scaleOp = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
		after = scaleOp.filter(img, after);

		return after;
	}

	public static BufferedImage crop(BufferedImage src, Rectangle rect) {
		BufferedImage dest = src.getSubimage(0, 0, rect.width, rect.height);
		return dest;
	}
}
