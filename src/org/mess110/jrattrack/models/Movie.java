package org.mess110.jrattrack.models;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.mess110.jrattrack.util.Util;

import com.xuggle.xuggler.ICodec;
import com.xuggle.xuggler.IContainer;
import com.xuggle.xuggler.IPacket;
import com.xuggle.xuggler.IPixelFormat;
import com.xuggle.xuggler.IStream;
import com.xuggle.xuggler.IStreamCoder;
import com.xuggle.xuggler.IVideoPicture;
import com.xuggle.xuggler.IVideoResampler;
import com.xuggle.xuggler.Utils;

public class Movie {

	private IStreamCoder videoCoder;
	private IContainer container;
	private IVideoResampler resampler;

	private MovieMeta meta;

	public Movie(MovieMeta meta) {
		this.meta = meta;
	}

	public void extractFrames() {
		meta.mkdirs();
		meta.writeMeta();
		try {
			decode();
			cleanup();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@SuppressWarnings("deprecation")
	private void decode() throws IOException {
		if (!IVideoResampler
				.isSupported(IVideoResampler.Feature.FEATURE_COLORSPACECONVERSION))
			throw new RuntimeException("you must install the GPL version"
					+ " of Xuggler (with IVideoResampler support) for "
					+ "this demo to work");

		container = IContainer.make();

		if (container.open(meta.getInputPath(), IContainer.Type.READ, null) < 0)
			throw new IllegalArgumentException("could not open file: "
					+ meta.getInputPath());

		int numStreams = container.getNumStreams();

		int videoStreamId = -1;
		videoCoder = null;
		for (int i = 0; i < numStreams; i++) {
			IStream stream = container.getStream(i);
			IStreamCoder coder = stream.getStreamCoder();

			if (coder.getCodecType() == ICodec.Type.CODEC_TYPE_VIDEO) {
				videoStreamId = i;
				videoCoder = coder;
				break;
			}
		}
		if (videoStreamId == -1)
			throw new RuntimeException(
					"could not find video stream in container: "
							+ meta.getInputPath());

		if (videoCoder.open() < 0)
			throw new RuntimeException(
					"could not open video decoder for container: "
							+ meta.getInputPath());

		resampler = null;
		if (videoCoder.getPixelType() != IPixelFormat.Type.BGR24) {
			// if this stream is not in BGR24, we're going to need to
			// convert it. The VideoResampler does that for us.
			resampler = IVideoResampler.make(videoCoder.getWidth(),
					videoCoder.getHeight(), IPixelFormat.Type.BGR24,
					videoCoder.getWidth(), videoCoder.getHeight(),
					videoCoder.getPixelType());
			if (resampler == null)
				throw new RuntimeException("could not create color space "
						+ "resampler for: " + meta.getInputPath());
		}

		IPacket packet = IPacket.make();
		long lastFrameAt = 0;
		while (container.readNextPacket(packet) >= 0) {
			if (packet.getStreamIndex() == videoStreamId) {
				long timestamp = Util.getTimestamp(packet);
				if (timestamp - lastFrameAt >= 1000 / meta.getAnalyzeFps()) {
					lastFrameAt = timestamp;
					BufferedImage bi = decodePacket(packet);
					if (bi != null) {
						saveBufferdImage(bi, timestamp);
					}
				}
			}
		}
		cleanup();
	}

	@SuppressWarnings("deprecation")
	private BufferedImage decodePacket(IPacket packet) {
		IVideoPicture picture = IVideoPicture.make(videoCoder.getPixelType(),
				videoCoder.getWidth(), videoCoder.getHeight());

		int offset = 0;
		while (offset < packet.getSize()) {
			int bytesDecoded = videoCoder.decodeVideo(picture, packet, offset);
			if (bytesDecoded < 0)
				throw new RuntimeException("got error decoding video in: "
						+ meta.getInputPath());
			offset += bytesDecoded;

			if (picture.isComplete()) {
				IVideoPicture newPic = picture;
				if (resampler != null) {
					newPic = IVideoPicture.make(
							resampler.getOutputPixelFormat(),
							picture.getWidth(), picture.getHeight());
					if (resampler.resample(newPic, picture) < 0)
						throw new RuntimeException(
								"could not resample video from: "
										+ meta.getInputPath());
				}
				if (newPic.getPixelType() != IPixelFormat.Type.BGR24)
					throw new RuntimeException("could not decode video"
							+ " as BGR 24 bit data in: " + meta.getInputPath());

				return Utils.videoPictureToImage(newPic);
			}
		}
		return null;
	}

	private void saveBufferdImage(BufferedImage bi, long timestamp)
			throws IOException {
		File outputfile = new File(meta.getFramesPath() + "/" + timestamp + ".png");
		outputfile.mkdirs();
		ImageIO.write(bi, "png", outputfile);
	}

	private void cleanup() {
		if (videoCoder != null) {
			videoCoder.close();
			videoCoder = null;
		}
		if (container != null) {
			container.close();
			container = null;
		}
	}

}
