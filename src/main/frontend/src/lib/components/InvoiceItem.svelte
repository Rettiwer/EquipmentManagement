<script lang="ts">
    import Input from "./Input.svelte";
    import InputDropdown from "$lib/components/InputDropdown.svelte";
    import type {Item} from "$lib/api/ItemEndpoint";
    import type {User} from "$lib/api/UserEndpoint";

    export let itemForm: Item;

    let ownerFullName = '';
    $: if (itemForm.owner != null) {
        ownerFullName = itemForm.owner.firstname + ', ' + itemForm.owner.lastname
    }

    let users: User[] = [];
    async function searchUser(e: any) {
        let name = e.target.value;
        if (!name) {
            users = [];
            return;
        }
        const response = await fetch('/api/users/search/' + name, {
                method: 'GET',
                headers: {
                    'content-type': 'application/json'
                }
            });

        users = await response.json();
    }
</script>
<div class="card card-compact w-full bg-base-300 shadow-md">
    <div class="card-body">
        <section class="flex flex-col content-around sm:flex-row">
            <Input
                    type="text"
                    label="Name"
                    placeholder="Name"
                    required
                    autocomplete="none"
                    bind:value={itemForm.name}
            />
            <Input
                    type="number"
                    label="Price [EUR]"
                    placeholder="Price"
                    required
                    autocomplete="none"
                    parentClass="!w-auto sm:ml-4"
                    bind:value={itemForm.price}
            />
        </section>

        <section class="flex flex-col content-around  sm:flex-row">
            <InputDropdown label="Owner"
                           placeholder="Search owner"
                           required
                           data={ users }
                           bind:displayValue={ownerFullName}
                           bind:value={itemForm.owner}
                           let:item
                           on:input={(e) => searchUser(e) }>

                                    <span class="mx-2 label-text text-base" data-item={JSON.stringify(item)}>
                                                {item.firstname}, {item.lastname}
                                    </span>
            </InputDropdown>
            <Input
                    type="text"
                    label="Comments"
                    placeholder="e.g. something was repaired"
                    autocomplete="none"
                    bind:value={itemForm.comment}
                    parentClass="flex-1 sm:ml-4"
            />
        </section>
    </div>
</div>
