package SampleProgram_FindingSpecification;

import Common.Activation;
import RDPCrystalEDILibrary.EDISpecification;

public class Program {

	public static void main(String[] args) {
		Activation.setLicense("<your_email>", "<your_javonet_serialnumber");
		// TODO Auto-generated method stub
        EDISpecification spec = EDISpecification.FindSpecificationFromFile("EDIFiles//SampleEDIFACT.txt");

        System.out.println(spec.getEDIFileType());
        System.out.println(spec.getEDIFACTReleaseVersion());
        System.out.println(spec.getEDIFACTMessageType());
        System.out.println(spec.getEDIFACTMessageVersion());

        System.out.println();

        EDISpecification spec2 = EDISpecification.FindSpecificationFromFile("EDIFiles//SampleEDIFile.txt");

        System.out.println(spec2.getEDIFileType());
        System.out.println(spec2.getX12TransactionSetCode());
        System.out.println(spec2.getX12Version());
        System.out.println(spec2.getX12VersionCode());

        System.out.println();
	}

}
