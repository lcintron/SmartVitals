using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using SmartVitals.Services.Enums;
using System.ComponentModel.DataAnnotations;

namespace SmartVitals_Server.Models
{
    

    public class UserInfo
    {
        [Required]
        public String UserName { get; set; }

        [Required(AllowEmptyStrings=false, ErrorMessage="*")]
        public String Name { get; set; }

        [Required(AllowEmptyStrings = false, ErrorMessage = "*")]
        [DataType(DataType.EmailAddress)]
        public String Email { get; set; }

        [Required]
        public String Sex { get; set; }

        [Required(AllowEmptyStrings = false, ErrorMessage = "*")]
        public Height Height { get; set; }

        [Required(AllowEmptyStrings = false, ErrorMessage = "*")]
        public int Weight { get; set; }

        [Required]
        public int Age { get; set; }

        [Required(AllowEmptyStrings = false, ErrorMessage = "*")]
        [DataType(DataType.PhoneNumber)]
        public string Phone { get; set; }

        [Required(AllowEmptyStrings = false, ErrorMessage = "*")]
        public String Preferences { get; set; }
        //public IEnumerable<Test> Tests { get; set; }
        
        public static UserInfo GetFromProfile(WebProfile profile)
        {
            return new UserInfo()
            {
                UserName = profile.UserName,
                Name = profile.Name,
                Sex = profile.Sex,
                Height = new Height
                {
                    Feet = profile.Height.Feet,
                    Inches = profile.Height.Inches
                },
                Age = profile.Age,
                Weight = profile.Weight,
                Preferences = profile.Preferences
                ,Phone = profile.Phone
                ,Email= profile.Email
            };
        }

        public static void Save(UserInfo profile)
        {
            WebProfile p = WebProfile.GetProfile(profile.UserName);
            p.Age = profile.Age;
            p.Email = profile.Email;
            p.Height.Feet = profile.Height.Feet;
            p.Height.Inches = profile.Height.Inches;
            p.Name = profile.Name;
            p.Phone = profile.Phone;
            p.Sex = profile.Sex;
            p.Preferences = profile.Preferences;
            p.Weight = profile.Weight;
            p.Save();
        }
    }
}
