using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace SmartVitals.Services.Enums
{
    public enum Roles {Administrator, User}
    public enum Sex { Male, Female }
    public enum Activity {Walk, Run, Other}
    public enum UserState {Rest, Excercise, PostExcercise }
    public enum VitalType { HeartRate
                    ,RespirationRate
                    ,SkinTemperature
                    ,Posture
                    ,BreathAmplitude
                    ,ECGAmplitude
                    ,SPO2
                    ,GalvanicSkinResponse
                    ,Acceleration}
}
