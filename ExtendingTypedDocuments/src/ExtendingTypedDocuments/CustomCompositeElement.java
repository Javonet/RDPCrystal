package ExtendingTypedDocuments;

    public class CustomCompositeElement implements CompositeElement
    {
        public String DiagnosisCodePointer1 { get; set; }
        public String DiagnosisCodePointer2 { get; set; }
        public String DiagnosisCodePointer3 { get; set; }
        public String DiagnosisCodePointer4 { get; set; }

        public override string GetString(char compElemSep)
        {
            List<string> e = new List<string>();

            e.Add(DiagnosisCodePointer1);
            e.Add(DiagnosisCodePointer2);
            e.Add(DiagnosisCodePointer3);
            e.Add(DiagnosisCodePointer4);

            return Write(compElemSep, e);
        }
    }
