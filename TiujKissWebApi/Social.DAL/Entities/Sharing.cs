using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Social.DAL.Entities
{
  public  class Sharing
    {
        [Key, DatabaseGenerated(DatabaseGeneratedOption.Identity)]
        public Guid SharingID { get; set; }
    }
}
