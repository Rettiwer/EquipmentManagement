<script lang="ts">
    import Button from "./Button.svelte";

    export let title: string, description: string, buttonTitle: string;

    let confirmModal: HTMLDialogElement;

    export function showModal() {
        confirmModal.showModal();
    }
</script>
<dialog bind:this={confirmModal} class="modal">
    <form method="dialog" class="modal-box">
        <h3 class="font-bold text-lg">{title}</h3>
        <p class="py-4">{description}</p>
        <div class="modal-action">
            <Button type="submit" on:click>
                {buttonTitle}
            </Button>
        </div>
    </form>
    <form method="dialog" class="modal-backdrop">
        <button>close</button>
    </form>
</dialog>
