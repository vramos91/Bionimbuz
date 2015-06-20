package br.unb.cic.bionimbus.services.storage.compress.compactors;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import org.apache.commons.io.IOUtils;

import br.unb.cic.bionimbus.services.storage.compress.Compactor;

public class GZipCompactor implements Compactor{

	@Override
	public File compact(File in, int compressionLevel) throws IOException {
		
		File out = new File("target/" + in.getName()+ ".gzip");
		GZIPOutputStream gzip;
		gzip = new GZIPOutputStream( new FileOutputStream(out));
		gzip.write(IOUtils.toByteArray(new FileReader(in)));
		IOUtils.closeQuietly(gzip);
		
		return out;
	}

	@Override
	public File descompact(File compressed) throws IOException {
		
		File out = new File("target/" + compressed.getName().replace(".gzip", ""));
		FileOutputStream fos = new FileOutputStream(out);
		byte[] buffer = new byte[(int)compressed.getTotalSpace()];
		
		GZIPInputStream gzip;
		gzip = new GZIPInputStream( new FileInputStream(compressed));
		
		int len;
        while ((len = gzip.read(buffer)) > 0) {
        	fos.write(buffer, 0, len);
        }
		fos.flush();
		IOUtils.closeQuietly(gzip);
		IOUtils.closeQuietly(fos);
		
		return out;
		
	}

	
	
}
