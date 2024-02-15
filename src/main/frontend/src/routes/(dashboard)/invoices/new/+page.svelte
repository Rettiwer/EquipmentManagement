<script lang="ts">
   import {IconArrowNarrowLeft} from "@tabler/icons-svelte";
   import Button from "$lib/components/Button.svelte";
   import Input from "$lib/components/Input.svelte";
   import InvoiceItem from "$lib/components/InvoiceItem.svelte";
   import type {Item} from "$lib/api/ItemEndpoint";
   import { invalidateAll, goto } from '$app/navigation';
   import { applyAction, deserialize } from '$app/forms';

   export let form;

   let items: Item[] = [];

   const itemForm = () => {
       return <Item>{
           id: '',
           name: '',
           price: '0',
           comment: '',
           owner: {
               id: '',
               firstname: '',
               lastname: '',
               email: '',
               roles: []
           },
           invoice: null,
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


   addItem();

   /** @param {{ currentTarget: EventTarget & HTMLFormElement}} event */
   async function handleSubmit(event: any) {
       const data = new FormData(event.currentTarget);
       data.append('items', JSON.stringify(items));

       const response = await fetch(event.currentTarget.action, {
           method: 'POST',
           body: data,
           headers: {
               'x-sveltekit-action': 'true'
           }
       });

       /** @type {import('@sveltejs/kit').ActionResult} */
       const result = deserialize(await response.text());


       if (result.type === 'success') {
           await invalidateAll();
       }

       applyAction(result);
   }

</script>

<svelte:head>
    <title>New invoice</title>
</svelte:head>

<main>
    <form class="flex flex-col xl:flex-row justify-around p-10" method="POST" on:submit|preventDefault={handleSubmit}>
        <section class="flex flex-col w-full max-w-4xl mb-10 sm:mr-10 sm:mb-0">
            <a href="/invoices" class="text-2xl font-bold mb-3 flex items-center cursor-pointer">
                <IconArrowNarrowLeft/>
                <span class="ml-3">Items</span>
            </a>
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
            <Button class="self-center" on:click={addItem}>
                Add item
            </Button>
        </section>


        <section class="w-full xl:w-fit">
            <h1 class="text-2xl font-bold mb-3">Invoice data</h1>
            <div class="card bg-base-300 shadow-md rounded-xl">
                <div class="card-body">

                    {#if !form?.success && form?.error.message != null }
                        <p class="text-red-500">{form.error.message}</p>
                    {/if}

                    <Input
                            type="text"
                            label="ID"
                            name="invoice_id"
                            placeholder="Invoice ID"
                            required
                            autocomplete="none"
                    />
                    <Input
                            type="date"
                            label="Date"
                            name="date"
                            required
                            autocomplete="none"
                    />

                    <div class="card-actions justify-end">
                        <Button class="ml-4" type="submit">
                            Save
                        </Button>
                    </div>
                </div>
            </div>
        </section>
    </form>
</main>
