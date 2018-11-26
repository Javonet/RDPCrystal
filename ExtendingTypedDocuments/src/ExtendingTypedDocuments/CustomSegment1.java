using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using RDPCrystalEDILibrary.Documents;
using RDPCrystalEDILibrary;

namespace SampleProgram_ExtendingTypedDocument
{
    public class CustomSegment1 : DocumentSegment
    {
        public CustomSegment1()
        {
            Name = "CS1";
        }

        public string ID { get; set; }
        public string RefID { get; set; }

        public override void Write(WriteArguments w)
        {
            base.Write(w);

            w.Values.Add(Name);
            w.Values.Add(ID);
            w.Values.Add(RefID);

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
                            ID = GetValue(element);
                            break;
                        }
                    case 1:
                        {
                            RefID = GetValue(element);
                            break;
                        }
                }
            }
        }
    }
}
