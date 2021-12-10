using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Mvc;
using System.ComponentModel.DataAnnotations;
using SmartVitals.Services.Models;

namespace SmartVitals_Server.Models
{
    public class Sample
    {
        public int SampleId { get; set; }
        public int TestId { get; set; }
        public String Time { get; set; }
        public double HeartRate { get; set; }
        public double RespirationRate { get; set; }
        public double Posture { get; set; }
        public double EKG { get; set; }
        public double BloodPressure { get; set; }
        public double SkinTemperature { get; set; }
        public double AccX { get; set; }
        public double AccY { get; set; }
        public double AccZ { get; set; }
    }
}
