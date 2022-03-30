package androidx.core.graphics;

import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.graphics.fonts.Font;
import android.graphics.fonts.FontFamily;
import android.graphics.fonts.FontStyle;
import android.os.CancellationSignal;
import android.os.ParcelFileDescriptor;
import androidx.core.content.res.FontResourcesParserCompat;
import androidx.core.provider.FontsContractCompat;
import java.io.IOException;
import java.io.InputStream;

public class TypefaceCompatApi29Impl extends TypefaceCompatBaseImpl {
    /* access modifiers changed from: protected */
    public FontsContractCompat.FontInfo findBestInfo(FontsContractCompat.FontInfo[] fonts, int style) {
        throw new RuntimeException("Do not use this function in API 29 or later.");
    }

    /* access modifiers changed from: protected */
    public Typeface createFromInputStream(Context context, InputStream is) {
        throw new RuntimeException("Do not use this function in API 29 or later.");
    }

    public Typeface createFromFontInfo(Context context, CancellationSignal cancellationSignal, FontsContractCompat.FontInfo[] fonts, int style) {
        int i;
        ParcelFileDescriptor pfd;
        FontFamily.Builder familyBuilder = null;
        ContentResolver resolver = context.getContentResolver();
        int length = fonts.length;
        int i2 = 0;
        int i3 = 0;
        while (true) {
            int i4 = 1;
            if (i3 < length) {
                FontsContractCompat.FontInfo font = fonts[i3];
                try {
                    pfd = resolver.openFileDescriptor(font.getUri(), "r", cancellationSignal);
                    if (pfd != null) {
                        Font.Builder weight = new Font.Builder(pfd).setWeight(font.getWeight());
                        if (!font.isItalic()) {
                            i4 = 0;
                        }
                        Font platformFont = weight.setSlant(i4).setTtcIndex(font.getTtcIndex()).build();
                        if (familyBuilder == null) {
                            familyBuilder = new FontFamily.Builder(platformFont);
                        } else {
                            familyBuilder.addFont(platformFont);
                        }
                        if (pfd != null) {
                            pfd.close();
                        }
                    } else if (pfd != null) {
                        pfd.close();
                    }
                } catch (IOException e) {
                } catch (Throwable th) {
                    th.addSuppressed(th);
                    break;
                }
                i3++;
            } else if (familyBuilder == null) {
                return null;
            } else {
                if ((style & 1) != 0) {
                    i = 700;
                } else {
                    i = 400;
                }
                if ((style & 2) != 0) {
                    i2 = 1;
                }
                return new Typeface.CustomFallbackBuilder(familyBuilder.build()).setStyle(new FontStyle(i, i2)).build();
            }
        }
        throw th;
    }

    public Typeface createFromFontFamilyFilesResourceEntry(Context context, FontResourcesParserCompat.FontFamilyFilesResourceEntry familyEntry, Resources resources, int style) {
        int i;
        FontFamily.Builder familyBuilder = null;
        FontResourcesParserCompat.FontFileResourceEntry[] entries = familyEntry.getEntries();
        int length = entries.length;
        int i2 = 0;
        int i3 = 0;
        while (true) {
            int i4 = 1;
            if (i3 >= length) {
                break;
            }
            FontResourcesParserCompat.FontFileResourceEntry entry = entries[i3];
            try {
                Font.Builder weight = new Font.Builder(resources, entry.getResourceId()).setWeight(entry.getWeight());
                if (!entry.isItalic()) {
                    i4 = 0;
                }
                Font platformFont = weight.setSlant(i4).setTtcIndex(entry.getTtcIndex()).setFontVariationSettings(entry.getVariationSettings()).build();
                if (familyBuilder == null) {
                    familyBuilder = new FontFamily.Builder(platformFont);
                } else {
                    familyBuilder.addFont(platformFont);
                }
            } catch (IOException e) {
            }
            i3++;
        }
        if (familyBuilder == null) {
            return null;
        }
        if ((style & 1) != 0) {
            i = 700;
        } else {
            i = 400;
        }
        if ((style & 2) != 0) {
            i2 = 1;
        }
        return new Typeface.CustomFallbackBuilder(familyBuilder.build()).setStyle(new FontStyle(i, i2)).build();
    }

    public Typeface createFromResourcesFontFile(Context context, Resources resources, int id, String path, int style) {
        try {
            Font font = new Font.Builder(resources, id).build();
            return new Typeface.CustomFallbackBuilder(new FontFamily.Builder(font).build()).setStyle(font.getStyle()).build();
        } catch (IOException e) {
            return null;
        }
    }
}
