using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

using RDPCrystalEDILibrary.Documents;
using RDPCrystalEDILibrary;

namespace SampleProgram_ExtendingTypedDocument
{
    public class CustomSegment3 : DocumentSegment
    {
        public CustomSegment3()
        {
            Name = "ABC";

            DiagnosisCodes = new CustomCompositeElement();
        }

        public string City { get; set; }
        public string State { get; set; }
        public string Zip { get; set; }
        public CustomCompositeElement DiagnosisCodes { get; set; }

        public override void Write(WriteArguments w)
        {
            base.Write(w);

            w.Values.Add(Name);
            w.Values.Add(City);
            w.Values.Add(State);
            w.Values.Add(Zip);
            w.Values.Add(DiagnosisCodes.GetString(w.Delimiters.CompositeTerminatorCharacter));

            WriteToBuffer(w);
        }

        public override void Load(LightWeightSegment segment)
        {
            for (int i = 0; i < segment.Elements.Count; i++)
            {
                LightWeightElement element = segment.Elements[i];

                switch (i)
                {
                    case 0:
                        {
                            City = GetValue(element);
                            break;
                        }
                    case 1:
                        {
                            State = GetValue(element);
                            break;
                        }
                    case 2:
                        {
                            Zip = GetValue(element);
                            break;
                        }
                    case 4:
                        {
                            if (element.Composite && element.Elements != null)
                            {
                                for (int j = 0; j < element.Elements.Count; j++)
                                {
                                    LightWeightElement celement = element.Elements[j];

                                    switch (j)
                                    {
                                        case 0:
                                            {
                                                DiagnosisCodes.DiagnosisCodePointer1 = GetValue(celement);
                                                break;
                                            }
                                        case 1:
                                            {
                                                DiagnosisCodes.DiagnosisCodePointer2 = GetValue(celement);
                                                break;
                                            }
                                        case 2:
                                            {
                                                DiagnosisCodes.DiagnosisCodePointer3 = GetValue(celement);
                                                break;
                                            }
                                        case 3:
                                            {
                                                DiagnosisCodes.DiagnosisCodePointer4 = GetValue(celement);
                                                break;
                                            }
                                    }
                                }
                            }
                            break;
                        }
                }
            }
        }
    }
}
