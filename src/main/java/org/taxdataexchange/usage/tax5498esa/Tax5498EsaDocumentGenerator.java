package org.taxdataexchange.usage.tax5498esa;

import org.taxdataexchange.core.utils.*;
import org.taxdataexchange.docs.tax5498esa.models.*;
import org.taxdataexchange.docs.tax5498esa.utils.*;

public class Tax5498EsaDocumentGenerator {

    public static TaxDataList sampleData( ) {

        // -----------------------------------------------------------------
        // Sample data
        // -----------------------------------------------------------------
        TaxData taxData = SampleData.sampleTaxData( "Tax5498Esa" );
        TaxDataList taxDataList = new TaxDataList( );
        taxDataList.addFormsItem( taxData );

        return taxDataList;

    }

    public static PdfOptions getPdfOptions( ) {

        // See https://www.taxdochub.com/pdf-options.html

        PdfOptions pdfOptions = new PdfOptions( );

        pdfOptions.setTriFold( true );
        pdfOptions.setWithAddresses( true );

        pdfOptions.setWithQrCode( true );
        pdfOptions.setQrStyle( PdfOptions.QR_STYLE_PORTRAIT );
        // pdfOptions.setQrStyle( PdfOptions.QR_STYLE_LANDSCAPE );

        pdfOptions.setWithJsonInside( true ) ;

        pdfOptions.setWithInstructions( true );

        pdfOptions.setWithITDStamp( false );
        // pdfOptions.setItdStampType( PdfOptions.ITD_TYPE_LOGO );
        // pdfOptions.setItdStampType( PdfOptions.ITD_TYPE_TEXT );

        pdfOptions.setMaskTin( true );

        return pdfOptions;

    }
    public static byte[] sampleQrAsPng( ) {

        // -----------------------------------------------------------------
        // Generate png
        // -----------------------------------------------------------------

        TaxDataList taxDataList = sampleData( );

        Tax5498EsaPdfBuilder pdfBuilder = new Tax5498EsaPdfBuilder( );
        byte[] bytes = pdfBuilder.buildQr( taxDataList );

        String filePath = "samples/Tax5498Esa.sample.png";
        FileUtils.bytesToFile( bytes, filePath );
        System.out.println( filePath );

        return bytes;

    }


    public static void samplePdf( ) {

        // -----------------------------------------------------------------
        // Generate PDF
        // -----------------------------------------------------------------

        TaxDataList taxDataList = sampleData( );

        String watermarkText = "Sample"; // Empty string for no watermark

        PdfOptions pdfOptions = getPdfOptions( );

        Tax5498EsaPdfBuilder pdfBuilder = new Tax5498EsaPdfBuilder( );

        byte[] pdfBytes = pdfBuilder.build( taxDataList, pdfOptions, watermarkText );

        String filePath = "samples/Tax5498Esa.sample.pdf";
        FileUtils.bytesToFile( pdfBytes, filePath );
        System.out.println( filePath );

    }

    public static void addTrailerPage() {

        String filePath = "samples/Tax5498Esa.original.pdf";
        byte[] pdfBytes = FileUtils.readByteArray( filePath );

        // QR code including frame. See Tax5498Esa.sample.png
        byte[] pngBytes = sampleQrAsPng( );

        // Place on existing pdf
        byte[] combinedBytes = TrailerPageAdder.insertPngIntoPdf( pngBytes, pdfBytes );
        String filePath2 = "samples/Tax5498Esa.trailer.pdf";
        FileUtils.bytesToFile( combinedBytes, filePath2 );

    }

    public static void addCoverPage() {

        String filePath = "samples/Tax5498Esa.original.pdf";
        byte[] pdfBytes = FileUtils.readByteArray( filePath );

        // QR code including frame. See Tax5498Esa.sample.png
        byte[] pngBytes = sampleQrAsPng( );

        // Place on existing pdf
        byte[] combinedBytes = CoverPageAdder.insertPngIntoPdf( pngBytes, pdfBytes );
        String filePath2 = "samples/Tax5498Esa.cover.pdf";
        FileUtils.bytesToFile( combinedBytes, filePath2 );

    }

    public static void insertQrCode() {

        // Existing document
        String filePath = "samples/Tax5498Esa.original.pdf";
        byte[] pdfBytes = FileUtils.readByteArray( filePath );

        // QR code including frame. See Tax5498Esa.sample.png
        byte[] pngBytes = sampleQrAsPng( );

        // Place on existing pdf
        int pageIndex = 0; // First page is zero
        float y = 72f; // From bottom. Pixels. 72 per inch.
        byte[] combinedBytes = PngInserter.insertPngIntoPdfAt(
            pngBytes,
            pdfBytes,
            pageIndex,
            y
        );
        String filePath2 = "samples/Tax5498Esa.inserted.pdf";
        FileUtils.bytesToFile( combinedBytes, filePath2 );

    }

    public static void main(String[] args) {

        System.out.println( "Tax5498EsaDocumentGenerator Begin" );

        // Create a new PDF
        samplePdf( );

        // Insert the QR code on your existing PDF at a specified location
        insertQrCode( );

        // Add cover page with QR code
        addCoverPage( );

        // Add trailer page with QR code
        addTrailerPage( );

        System.out.println( "Tax5498EsaDocumentGenerator Done" );

    }

}
