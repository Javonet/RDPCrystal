using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using RDPCrystalEDILibrary.Documents;
using RDPCrystalEDILibrary;

namespace SampleProgram_ExtendingTypedDocument
{
    public class CustomSegment2 : DocumentSegment
    {
        public CustomSegment2()
        {
            Name = "CS2";
        }

        public string SocialSecurity { get; set; }
        public string Age { get; set; }

        public override void Write(WriteArguments w)
        {
            base.Write(w);

            w.Values.Add(Name);
            w.Values.Add(SocialSecurity);
            w.Values.Add(Age);

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
                            SocialSecurity = GetValue(element);
                            break;
                        }
                    case 1:
                        {
                            Age = GetValue(element);
                            break;
                        }
                }
            }
        }
    }
}
