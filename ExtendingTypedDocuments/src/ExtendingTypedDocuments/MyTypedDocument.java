using RDPCrystalEDILibrary.Documents;

namespace SampleProgram_ExtendingTypedDocument
{
    public class MyTypedDocument : TypedEDIDocument 
    {
        public MyTypedDocument()
        {
        }

        /// <summary>
        /// This method is only called when you are loading this document from an EDILightWeightDocument
        /// </summary>
        /// <param name="segmentName"></param>
        /// <returns></returns>
        protected override DocumentSegment GetDocumentSegment(string segmentName)
        {
            switch (segmentName)
            {
                case "CS1":
                    {
                        return new CustomSegment1();
                    }
                case "CS2":
                    {
                        return new CustomSegment2();
                    }
                case "ABC":
                    {
                        return new CustomSegment3();
                    }
            }

            return null;
        }
    }
}
