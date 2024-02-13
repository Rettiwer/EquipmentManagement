<script lang="ts">
   /* export let status ='';
    export let errors = {};
    export let invoiceData = [];

    let deleteInvoiceModal;

    const itemForm = () => {
        return {
            id: null,
            name: null,
            price: null,
            comments: null,
            liquidated_at: null,
            user: {id: null, full_name: null}
        }
    }

    let invoiceForm = useForm({
        id: invoiceData.id,
        invoice_id: invoiceData.invoice_id,
        invoice_date: invoiceData.invoice_date,
        evidence_id: invoiceData.evidence_id,
        items: invoiceData.items ? invoiceData.items : [itemForm()],
    });

    const addItem = () => {
        $invoiceForm.items = [...$invoiceForm.items, itemForm()];
    }

    const removeItem = (itemId) => () => {
        if ($invoiceForm.items.length > 1) {
            $invoiceForm.items.splice(itemId, 1);
            $invoiceForm.items = $invoiceForm.items;
        } else {
            $invoiceForm.id ? deleteInvoiceModal.showModal() : $invoiceForm.items[0] = itemForm();
        }
    }

    const onSubmit = () => {
        if (!$invoiceForm.id) {
            $invoiceForm.post(window.route('invoices.store'), {
                onSuccess: () => {
                    $invoiceForm.reset();
                    addItem();
                },
            });
        } else {
            $invoiceForm.put(window.route('invoices.update', $invoiceForm.id));
        }
    };

    function deleteInvoice() {
        $invoiceForm.delete(window.route('invoices.destroy', $invoiceForm.id));
    }
*/


    import {IconArrowNarrowLeft} from "@tabler/icons-svelte";
   import Button from "$lib/components/Button.svelte";
   import Input from "$lib/components/Input.svelte";
   import InvoiceItem from "$lib/components/InvoiceItem.svelte";
   import {onMount} from "svelte";
   import type {Item} from "$lib/api/user";

   export let form;
   export let items: Item[];

   const itemForm = () => {
       return <Item>{
           id: '',
           name: '',
           price: '0',
           comment: '',
           owner: null,
           invoiceId: '',
       }
   }

   const addItem = () => {
       items = [...items, itemForm()];
   }

   const removeItem = (itemId: number) => () => {
       if (items.length > 1) {
           items.splice(itemId, 1);
           items = items;
       } else {
           items[0] = itemForm();
       }
   }

   onMount(() => {
       addItem();
   })

</script>

<svelte:head>
    <title>New invoice</title>
</svelte:head>

<main>
    <form class="flex flex-col xl:flex-row justify-around p-10">
        <section class="flex flex-col w-full max-w-4xl mb-10 sm:mr-10 sm:mb-0">
            <h1 class="text-2xl font-bold mb-3 flex items-center cursor-pointer">
<!--                on:click={() => router.get(window.route('invoices.index'), {}, {replace:true})}>-->
                <IconArrowNarrowLeft/>
                <span class="ml-3">Items</span>
            </h1>
            { #each items as item, i }
                <section class="mb-4">
                    <div class="flex items-center mb-4">
                        <h1 class="text-2xl font-bold">#{i + 1}</h1>
                        <Button class="btn-xs ml-4" outlined on:click={removeItem(i)}>
                            DELETE
                        </Button>
                    </div>

                    <InvoiceItem itemForm={item}/>
                </section>
            { /each }
<!--            <Button class="self-center" on:click={addItem}>-->
<!--                Dodaj pozycje-->
<!--            </Button>-->
        </section>


        <section class="w-full xl:w-fit">
            <h1 class="text-2xl font-bold mb-3">Dane faktury</h1>
            <div class="card bg-base-300 shadow-md rounded-xl">
                <div class="card-body">
<!--                    <ValidationErrors class="mb-4" errors={errors}/>-->

                    <Input
                            type="text"
                            label="Numer"
                            placeholder="Numer Faktury"
                            required
                            autocomplete="none"
                    />
                    <Input
                            type="date"
                            label="Data"
                            required
                            autocomplete="none"
                    />
                    <Input
                            type="text"
                            label="Numer Ewidencyjny"
                            placeholder="Numer Ewidencyjny"
                            required
                            autocomplete="none"
                    />
                    <div class="card-actions justify-end">
                        <!--{#if $invoiceForm.id}-->
                        <!--    <Button class="ml-4" outlined on:click={() => deleteInvoiceModal.showModal()}>-->
                        <!--        Usu?-->
                        <!--    </Button>-->
                        <!--{ /if }-->
<!--                        <Button class="ml-4" type="submit" disabled={$invoiceForm.processing}>-->
<!--                            Zapisz-->
<!--                        </Button>-->
                    </div>
                </div>
            </div>
        </section>
    </form>
</main>
