<script lang="ts">
    import {IconX} from "@tabler/icons-svelte";

    export let label: string, placeholder: string, data: any, value: any, displayValue: string | null;

    let searchInputElement: HTMLInputElement, dropdownElement: HTMLElement;

    function selectItem(e: any) {
        let item = e.currentTarget.getElementsByTagName('span')[0];
        displayValue = item.innerHTML;
        value = JSON.parse(item.dataset.item);
        dropdownElement.blur();
    }

    function reset() {
        data = null;
        displayValue = null;
        value = null;
        searchInputElement.value = '';
    }

    $: if (value === null) {
        data = null;
        displayValue = null;
    }

</script>
<div class="form-control w-auto">
    <label class="label" for="">
        <span class="label-text">{ label } {@html $$restProps.required ? '<span class="text-primary">*</span>' : ''}</span>
    </label>

    <div class="dropdown w-full mb-4 bg-base-100">
        <div class="flex items-center outline outline-1 outline-neutral rounded-lg">
            <input
                    type="text"
                    tabIndex={0}
                    bind:value={ displayValue }
                    on:keydown={() => searchInputElement.focus()}
                    placeholder="{ placeholder }"
                    autocomplete="none"
                    class="input focus:outline-none focus:border-0 w-full"
                    {...$$restProps}
            />

            <!-- A11y: visible, non-interactive elements with an on:click event must be accompanied by a keyboard event handler. -->
            <div class="btn btn-xs btn-circle btn-secondary mx-3" on:click={() => reset()} class:invisible={!displayValue}>
                <IconX/>
            </div>
        </div>
        <!-- A11y: noninteractive element cannot have nonnegative tabIndex value -->
        <div tabindex="0" class="w-full dropdown-content p-4 shadow-lg bg-base-100 rounded-box mt-1 z-10"
             bind:this={dropdownElement}>

            <!-- search input -->
            <input
                    on:input
                    bind:this={searchInputElement}
                    type="text"
                    placeholder="Search"
                    autocomplete="none"
                    class="input input-sm input-bordered w-full"/>

            <!-- spacer -->
            <div class="h-4"></div>

            {#if data}
                {#each data as item}
                    <div class="label cursor-pointer hover:bg-accent/10 rounded-lg "
                         on:click={(e) => selectItem(e)}>
                        <slot {item}></slot>
                    </div>
                {/each}
            {/if}

            <!-- spacer -->
            <div class="h-4"></div>
        </div>
    </div>
</div>
