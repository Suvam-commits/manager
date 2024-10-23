console.log("contact.js");
const baseURL = "http://localhost:8081";

const viewContactModal = document.getElementById('view_contact_modal');

// options with default values
const options = {
    placement: 'bottom-right',
    backdrop: 'dynamic',
    backdropClasses:
        'bg-gray-900/50 dark:bg-gray-900/80 fixed inset-0 z-40',
    closable: true,
    onHide: () => {
        console.log('modal is hidden');
    },
    onShow: () => {
        console.log('modal is shown');
    },
    onToggle: () => {
        console.log('modal has been toggled');
    },
};

// instance options object
const instanceOptions = {
  id: 'view_contact_modal',
  override: true
};

const contactModal = new Modal(viewContactModal,options,instanceOptions);

function openContactModal(){
    contactModal.show();
}

function closeContactModel(){
    contactModal.hide();
}

async function loadContactdata(id){
    console.log(id);

    try {
        const data = await(await fetch(`${baseURL}/api/contacts/${id}`))
        .json();

        console.log(data);
        document.querySelector("#contact_name").innerHTML=data.name; 
        document.querySelector("#contact_email").innerHTML=data.email;
        document.querySelector("#contact_image").src = data.picture;
        document.querySelector("#contact_adress").innerHTML=data.adress;
        document.querySelector("#contact_phone").innerHTML=data.phoneNumber; 
        document.querySelector("#contact_about").innerHTML=data.description;
        const contactFavourite = document.querySelector("#contact_favourite");
        if(data.favourite){
            contactFavourite.innerHTML=
            "<i class='fas fa-star text-yellow-400'></i>"
        }else{
            contactFavourite.innerHTML = "Not favourite contact"
        }

        document.querySelector("#contact_website").href = data.websiteLink;
        document.querySelector("#contact_website").innerHTML = data.websiteLink;
        document.querySelector("#contact_linkedIn").href = data.linkedInLink;
        document.querySelector("#contact_website").innerHTML = data.linkedInLink;

        openContactModal();
    } catch (error) {
        console.log("Error",error);
    }
}

async function deleteContact(id) {
    Swal.fire({
        title: "Do you want to delete the contact?",
        icon:"warning",
        showCancelButton: true,
        confirmButtonText: "Delete",
      }).then((result) => {
        /* Read more about isConfirmed, isDenied below */
        if (result.isConfirmed) {
            const url = `${baseURL}/user/contacts/delete/`+id;
            window.location.replace(url);
         }
      });      
}

 
