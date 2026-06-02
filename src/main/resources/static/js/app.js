/**
 * Rich Text Editor Initialization
 */
const quill = new Quill(
    '#editor',
    {
        theme: 'snow'
    }
);

const form =
    document.getElementById('emailForm');

const loader =
    document.getElementById('loader');

const sendButton =
    document.getElementById('sendButton');

const alertContainer =
    document.getElementById('alertContainer');

/**
 * Email validation regex.
 */
const emailPattern =
    /^[^\s@]+@[^\s@]+\.[^\s@]+$/;

/**
 * Validate comma separated emails.
 */
function validateEmails(emailString) {

    if (!emailString) {
        return true;
    }

    const emails =
        emailString.split(',');

    for (const email of emails) {

        if (!emailPattern.test(email.trim())) {

            return false;
        }
    }

    return true;
}

/**
 * Show alert.
 */
function showAlert(
    message,
    type
) {

    alertContainer.innerHTML =
        `
        <div class="alert alert-${type}">
            ${message}
        </div>
        `;
}

/**
 * Form Submission
 */
form.addEventListener(
    'submit',
    async function (event) {

        event.preventDefault();

        const to =
            document.getElementById('to').value;

        const cc =
            document.getElementById('cc').value;

        const bcc =
            document.getElementById('bcc').value;

        const subject =
            document.getElementById('subject').value;

        const body =
            quill.root.innerHTML;

        if (!validateEmails(to)) {

            showAlert(
                'Invalid TO email address.',
                'danger'
            );

            return;
        }

        if (!validateEmails(cc)) {

            showAlert(
                'Invalid CC email address.',
                'danger'
            );

            return;
        }

        if (!validateEmails(bcc)) {

            showAlert(
                'Invalid BCC email address.',
                'danger'
            );

            return;
        }

        document
            .getElementById('body')
            .value = body;

        const formData =
            new FormData(form);

        loader.classList.remove('d-none');

        sendButton.disabled = true;

        try {

            const response =
                await fetch(
                    '/api/emails/send',
                    {
                        method: 'POST',
                        body: formData
                    }
                );

            const result =
                await response.json();

            if (response.ok) {

                showAlert(
                    result.message,
                    'success'
                );

                form.reset();

                quill.setText('');

            } else {

                showAlert(
                    result.message,
                    'danger'
                );
            }

        } catch (error) {

            showAlert(
                'Unable to connect to server.',
                'danger'
            );

            console.error(error);

        } finally {

            loader.classList.add('d-none');

            sendButton.disabled = false;
        }
    });