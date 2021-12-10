using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace SmartVitals_Server.Models
{
    public class Height
    {
        public int Feet { get; set; }
        public int Inches { get; set; }

        public String ToString()
        {
            return Feet + "' " + Inches + "''";
        }
    }
}
